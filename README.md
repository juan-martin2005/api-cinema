# api-cinema

API de cines, películas, salas, funciones y reservas con Java 21, Spring Boot 4,
PostgreSQL, Redis y Stripe Checkout. Arquitectura por módulos con puertos y adaptadores.

## Arranque local

1. Instalar Java 21 y, para los servicios locales, Docker con Compose.
2. Crear `.env` a partir de `.env.example` **solo si no existe**. Adaptar las variables
   a una base de desarrollo aislada; el archivo existente no se sobrescribe.
3. Generar `JWT_SECRET_KEY` con `openssl rand -base64 48` y guardarla en `.env`.
4. Usar `SPRING_PROFILES_ACTIVE=dev`. Para el catálogo de demostración, configurar
   `DEMO_ADMIN_PASSWORD` con 8–72 caracteres. El usuario será `admin@example.test`.
   Las cuentas de demostración no contienen contraseñas utilizables en el repositorio.
5. Ejecutar `docker compose up -d` y `./mvnw spring-boot:run`
   (`.\mvnw.cmd spring-boot:run` en PowerShell).

Compose expone PostgreSQL en `15432`, Redis en `16379` y SMTP en `11025`,
únicamente en localhost. Los correos de desarrollo se consultan en
`http://localhost:18025`. Revisar que `.env` utiliza estos puertos.

Swagger: `http://localhost:8080/swagger-ui/index.html`.

## Datos y perfiles

- Sin perfil y en `prod`: Flyway crea el esquema en una base vacía y Hibernate lo
  valida. No se ejecuta `create`, `update`, `clean` ni un reinicio automático.
- `dev`: añade el catálogo de demostración con funciones relativas a la fecha de
  inicialización. Sus datos se cargan una sola vez, como migración Flyway.
- `test`: H2 aislado, correo y tareas programadas desactivados; nunca usa la base
  de `.env`. Las pruebas con Testcontainers sustituyen H2 por PostgreSQL y Redis.
- Una base antigua sin historial Flyway se rechaza intencionalmente. Para este
  proyecto se acordó usar una base nueva de desarrollo. No activar
  `baseline-on-migrate` sobre un esquema antiguo: requiere una migración específica.

El volumen `cinema_dev_data` conserva datos entre arranques. Eliminarlo es una
operación manual que no forma parte del arranque ni de las pruebas.

## Autenticación y autorización

`POST /api/auth/register`, `/login`, `/refreshToken` y `/logout` conservan su función.
Las contraseñas requieren 8–72 caracteres. Los correos se normalizan y las cuentas
deben estar verificadas y activas. Los permisos se consultan en cada solicitud JWT.
Desactivar una cuenta o cambiar la contraseña revoca sus sesiones.

Login permite 10 intentos por correo cada 15 minutos. OTP permite 5 intentos por
código y un reenvío por minuto. Los correos se encolan en una outbox persistente,
se envían después del commit y se reintentan hasta cinco veces. En producción,
configurar SMTP autenticado y TLS. Consultar registros de fallos de entrega.

OAuth2 requiere las credenciales Google/Facebook configuradas localmente. El callback
redirige a `REDIRECT_URL?code=...`: el código dura dos minutos y se intercambia una
sola vez mediante `POST /api/auth/oauth/exchange` con `{"code":"..."}`.
Si el correo ya pertenece a una cuenta local, primero iniciar sesión y luego usar
`POST /api/auth/oauth/link` con Bearer y el código. No se vincula por correo de forma
automática ni se envían access/refresh tokens en la URL.

## Reservas y pagos Stripe

1. Crear una función con `movieId`, `cinemaRoomId`, **`formatId`** y `startTime`.
   El formato debe pertenecer al cine; la sala no determina el precio por su nombre.
2. `POST /api/bookings` con `{"showTimeId":"UUID","seatIds":["UUID"]}` crea
   `PENDING/UNPAID` y ocupa los asientos transaccionalmente. Máximo 10 asientos.
   Deben faltar al menos 35 minutos para la función.
3. `POST /api/bookings/{id}/checkout` devuelve `sessionId`, `url` y `expiresAt`.
   El frontend abre `url`; no envía el importe ni recibe datos de tarjeta.
4. Stripe confirma el pago mediante `POST /api/payments/stripe/webhook`, con la
   cabecera `Stripe-Signature`. El servidor verifica firma, sesión, reserva, importe
   y moneda. La URL de retorno no confirma pagos. Los eventos repetidos son idempotentes.
5. `GET /api/bookings/{id}` consulta el resultado. Propietario o `ADMIN` pueden
   consultar/cancelar; el listado global es administrativo.

El catálogo y el cobro usan **PEN**, sin conversión automática. El precio de formato
es subtotal por entrada; se añade la tasa configurable `TAX_RATE` (predeterminada
`0.18`). Se guardan la tarifa y los importes aplicados. El detalle de reserva incluye
`currency`, `expiredAt` y estados tipados de pago.

La reserva inicial dura 30 minutos. Al crear Checkout, se utiliza un vencimiento
persistido de 31 minutos para respetar el mínimo de Stripe incluso con latencia.
Los reintentos usan el mismo intento, vencimiento y clave de idempotencia.
Las tareas reconcilian cada 30 segundos. Una sesión ambigua conserva su ocupación
hasta resolver el estado con Stripe; Redis no controla la disponibilidad.

`PUT /api/bookings/{id}/confirm` permanece restringido a `ADMIN`, pero ahora solo
reconcilia un pago verificable: devuelve `409` si no está pagado. No permite marcar
pagos manualmente. Se rechazan cambios de horario/sala/película con reservas activas
y cambios de duración de películas con funciones futuras.

Cancelar antes del inicio cierra Checkout o solicita devolución completa. Estados:
`UNPAID`, `PAID`, `CANCEL_PENDING`, `REFUND_PENDING`, `REFUNDED`, `FAILED`.
Una devolución pendiente no se presenta como completada. Los fallos se reintentan
con claves persistentes; los asientos se liberan al resolver la cancelación/devolución.
Un pago tardío sobre una reserva cancelada genera devolución y nunca reasigna asientos.

### Sandbox

Configurar en `.env`:

```properties
STRIPE_ENABLED=true
STRIPE_ALLOW_LIVE=false
STRIPE_SECRET_KEY=sk_test_REEMPLAZAR_LOCALMENTE
STRIPE_WEBHOOK_SECRET=whsec_REEMPLAZAR_LOCALMENTE
STRIPE_SUCCESS_URL=http://localhost:3000/payment/success?session_id={CHECKOUT_SESSION_ID}
STRIPE_CANCEL_URL=http://localhost:3000/payment/cancel
```

Con Stripe CLI: `stripe listen --forward-to localhost:8080/api/payments/stripe/webhook`.
Guardar el secreto emitido por la CLI en `.env` y reiniciar la API. Crear una reserva,
abrir Checkout y usar las tarjetas de prueba de Stripe. Para este flujo usar un
checkout real del sandbox: un evento genérico de CLI no contiene los identificadores
de reserva de la aplicación.

Sin claves, `/checkout` devuelve `503`; no simula un pago exitoso. La integración se
entrega para sandbox. La activación comercial y la compatibilidad de PEN deben
validarse con la cuenta elegible; Perú no aparece actualmente como país de apertura
de cuenta en [Stripe Global](https://stripe.com/global).

Referencias: [Checkout e inventario](https://docs.stripe.com/api/checkout/sessions/create),
[confirmación idempotente](https://docs.stripe.com/checkout/fulfillment),
[firmas](https://docs.stripe.com/webhooks/signatures).

## Cambios del contrato

- Actualizar una sala utiliza únicamente `{"name":"Nuevo nombre"}`. No modifica
  silenciosamente cine, capacidad o distribución.
- Crear/actualizar funciones requiere `formatId`. Sus respuestas incluyen formato,
  hora de inicio y fin. Solo se admiten inicios desde las 10:00 hasta antes de las 23:00,
  en `America/Lima`; la proyección puede terminar después del cierre de admisión.
- Las reservas nuevas son pendientes. No asumir `PAID` en la respuesta de creación.
- Los endpoints paginados consultan PostgreSQL y respetan ordenación. Las reservas
  se ordenan por fecha descendente; máximo 100 por página.
- `403`: reserva ajena; `409`: asiento ocupado, transición inválida o pago no
  confirmado; `429`: límite de autenticación; `502/503`: Stripe no disponible/configurado.

## Pruebas y CI

Ejecutar `./mvnw clean verify` o `.\mvnw.cmd clean verify`. Las pruebas locales
incluyen arranque, reservas concurrentes, horarios solapados, seguridad JWT/OAuth,
refresh tokens, OTP, coordenadas, caché, webhook duplicado, firma manipulada,
timeouts, vencimientos y reembolsos pendientes.

Con Docker se ejecuta la misma suite contra PostgreSQL 17 y Redis 7 aislados,
incluidas las migraciones Flyway. Sin Docker se omite explícitamente esa suite.
GitHub Actions ejecuta ambas y conserva los reportes de Surefire.

Los secretos y `target/` están excluidos de Git. Los logs de reconciliación contienen
identificadores de reserva y tipos de error, sin claves ni datos de tarjeta. En una
instalación operativa, supervisar reservas retenidas, intentos pendientes y correos
que hayan agotado reintentos. No se incluye frontend ni despliegue comercial.
