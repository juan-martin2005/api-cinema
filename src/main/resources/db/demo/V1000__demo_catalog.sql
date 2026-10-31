--



-- =============================================================================
-- 2. DEMO USERS (development only; replace password through local setup)
-- =============================================================================
INSERT INTO users (id, created_at, email, is_active, is_verified, last_name, name, password) VALUES ('55b97056-63d7-45cc-9fe0-1e7c70676875', NOW(), 'admin@example.test', true, true, 'ADMIN', 'JUAN', '{disabled}');

INSERT INTO users (id, created_at, email, is_active, is_verified, last_name, name, password) VALUES ('66c97056-63d7-45cc-9fe0-1e7c70676876', NOW(), 'cliente@cinema.com', true, true, 'PEREZ', 'CARLOS', '{disabled}');

-- Roles asignados
INSERT INTO user_roles (user_id, role_id) VALUES ('55b97056-63d7-45cc-9fe0-1e7c70676875', 2);
INSERT INTO user_roles (user_id, role_id) VALUES ('55b97056-63d7-45cc-9fe0-1e7c70676875', 1);
INSERT INTO user_roles (user_id, role_id) VALUES ('66c97056-63d7-45cc-9fe0-1e7c70676876', 1);

-- =============================================================================
-- 3. FORMATOS DE PROYECCION (FORMATS)
-- =============================================================================
INSERT INTO formats (id, name, price) VALUES (1, '2D EstÃƒÂ¡ndar', 18.00);
INSERT INTO formats (id, name, price) VALUES (2, '3D Digital', 24.00);
INSERT INTO formats (id, name, price) VALUES (3, 'IMAX Laser', 34.00);
INSERT INTO formats (id, name, price) VALUES (4, '4DX DinÃƒÂ¡mica', 40.00);
INSERT INTO formats (id, name, price) VALUES (5, 'VIP Prime Lounge', 32.00);

-- =============================================================================
-- 4. CINES (CINEMAS) - 2 CINES
-- =============================================================================
-- Cine 1: CinePlanet Prime Plaza San Miguel (Lima)
INSERT INTO cinemas (id, name, img, address, second_address, city, latitude, longitude, room_quantity, description) VALUES ('a1111111-1111-1111-1111-111111111111', 'CinePlanet Prime San Miguel', 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800', 'Av. La Marina 2000', 'C.C. Plaza San Miguel - Nivel 3', 'Lima', -12.077222, -77.080556, 4, 'Complejo moderno equipado con salas IMAX Laser, 4DX y sonido Dolby Atmos envolvente, zona de dulcerÃƒÂ­a premium y butacas reclinables de cuero.');

-- Cine 2: CineMark Grand Jockey Plaza (Lima)
INSERT INTO cinemas (id, name, img, address, second_address, city, latitude, longitude, room_quantity, description) VALUES ('a2222222-2222-2222-2222-222222222222', 'CineMark Grand Jockey Mall', 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=800', 'Av. Javier Prado Este 4200', 'Jockey Plaza Shopping Center - Nave Central', 'Lima', -12.086389, -76.974444, 4, 'Experiencia cinematogrÃƒÂ¡fica de primer nivel con salas VIP, servicio gourmet directo a la butaca, proyecciÃƒÂ³n lÃƒÂ¡ser 4K y sonido envolvente.');

-- =============================================================================
-- 5. FORMATOS POR CINE (CINEMA_FORMATS)
-- =============================================================================
-- Formatos Cine 1
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a1111111-1111-1111-1111-111111111111', 1);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a1111111-1111-1111-1111-111111111111', 2);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a1111111-1111-1111-1111-111111111111', 3);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a1111111-1111-1111-1111-111111111111', 4);

-- Formatos Cine 2
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a2222222-2222-2222-2222-222222222222', 1);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a2222222-2222-2222-2222-222222222222', 2);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a2222222-2222-2222-2222-222222222222', 3);
INSERT INTO cinema_formats (cinema_id, format_id) VALUES ('a2222222-2222-2222-2222-222222222222', 5);

-- =============================================================================
-- 6. SALAS DE CINE (CINEMA_ROOMS)
-- =============================================================================
-- Salas Cine 1 (San Miguel)
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b1111111-1111-1111-1111-111111111101', 'Sala 1 - 2D Atmos', 32, 8, 'a1111111-1111-1111-1111-111111111111');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b1111111-1111-1111-1111-111111111102', 'Sala 2 - 3D Digital', 32, 8, 'a1111111-1111-1111-1111-111111111111');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b1111111-1111-1111-1111-111111111103', 'Sala 3 - IMAX Laser', 32, 8, 'a1111111-1111-1111-1111-111111111111');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b1111111-1111-1111-1111-111111111104', 'Sala 4 - 4DX Dynamic', 32, 8, 'a1111111-1111-1111-1111-111111111111');

-- Salas Cine 2 (Jockey Mall)
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b2222222-2222-2222-2222-222222222201', 'Sala 1 - 2D Premier', 32, 8, 'a2222222-2222-2222-2222-222222222222');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b2222222-2222-2222-2222-222222222202', 'Sala 2 - 3D XD', 32, 8, 'a2222222-2222-2222-2222-222222222222');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b2222222-2222-2222-2222-222222222203', 'Sala 3 - IMAX GT', 32, 8, 'a2222222-2222-2222-2222-222222222222');
INSERT INTO cinema_rooms (id, name, capacity, seat_per_row, cinema_id) VALUES ('b2222222-2222-2222-2222-222222222204', 'Sala 4 - VIP Lounge', 32, 8, 'a2222222-2222-2222-2222-222222222222');

-- =============================================================================
-- 7. BUTACAS (SEATS) - 32 BUTACAS POR SALA (Filas A, B, C, D x 8 asientos)
-- =============================================================================
-- Asientos Sala 1
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b1111111-1111-1111-1111-111111111101');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0100000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b1111111-1111-1111-1111-111111111101');

-- Asientos Sala 2
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b1111111-1111-1111-1111-111111111102');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0200000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b1111111-1111-1111-1111-111111111102');

-- Asientos Sala 3
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b1111111-1111-1111-1111-111111111103');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0300000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b1111111-1111-1111-1111-111111111103');

-- Asientos Sala 4
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b1111111-1111-1111-1111-111111111104');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0400000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b1111111-1111-1111-1111-111111111104');

-- Asientos Sala 5
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b2222222-2222-2222-2222-222222222201');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0500000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b2222222-2222-2222-2222-222222222201');

-- Asientos Sala 6
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b2222222-2222-2222-2222-222222222202');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0600000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b2222222-2222-2222-2222-222222222202');

-- Asientos Sala 7
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b2222222-2222-2222-2222-222222222203');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0700000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b2222222-2222-2222-2222-222222222203');

-- Asientos Sala 8
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000001', 1, 1, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000002', 2, 2, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000003', 3, 3, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000004', 4, 4, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000005', 5, 5, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000006', 6, 6, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000007', 7, 7, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000008', 8, 8, 1, 'A', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000009', 1, 1, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000010', 2, 2, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000011', 3, 3, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000012', 4, 4, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000013', 5, 5, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000014', 6, 6, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000015', 7, 7, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000016', 8, 8, 2, 'B', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000017', 1, 1, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000018', 2, 2, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000019', 3, 3, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000020', 4, 4, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000021', 5, 5, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000022', 6, 6, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000023', 7, 7, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000024', 8, 8, 3, 'C', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000025', 1, 1, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000026', 2, 2, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000027', 3, 3, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000028', 4, 4, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000029', 5, 5, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000030', 6, 6, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000031', 7, 7, 4, 'D', 'b2222222-2222-2222-2222-222222222204');
INSERT INTO seats (id, number, pos_x, pos_y, "row", room_id) VALUES ('e0800000-0000-0000-0000-000000000032', 8, 8, 4, 'D', 'b2222222-2222-2222-2222-222222222204');

-- =============================================================================
-- =============================================================================
-- 8. PELICULAS (MOVIES) - 20 TITULOS (Fechas desde 2026-08-20 en adelante)
-- =============================================================================

-- 1. Dune: Parte Dos
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000001', 'Dune: Parte Dos', 'Paul Atreides se une a Chani y a los Fremen mientras busca venganza contra los conspiradores que destruyeron a su familia. Ante una eleccion entre el amor de su vida y el destino del universo, luchara por evitar un futuro terrible.', 'https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg', '+14', 166, 'Ciencia Ficcion / Aventura', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 2. Deadpool & Wolverine
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000002', 'Deadpool & Wolverine', 'Un apatico Wade Wilson se afana en la vida civil tras dejar atras sus dias como Deadpool. Pero cuando su mundo natal se enfrenta a una amenaza existencial, debe volver a enfundarse el traje junto a un reticente Wolverine.', 'https://image.tmdb.org/t/p/w500/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg', '+18', 128, 'Accion / Comedia', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 3. IntensaMente 2
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000003', 'IntensaMente 2', 'Regresa a la mente de Riley reciÃƒÂ©n entrada en la adolescencia justo cuando su cuartel general sufre una repentina demolicion para dar paso a nuevas emociones: Ansiedad, Envidia, Ennui y Verguenza.', 'https://image.tmdb.org/t/p/w500/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg', 'ATP', 96, 'Animacion / Familiar', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 4. Gladiador 2
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000004', 'Gladiador 2', 'Anos despues de presenciar la muerte del admirado heroe Maximo, Lucio debe ingresar al Coliseo tras ser testigo de la conquista de su hogar por parte de los tiranicos emperadores que ahora dirigen Roma.', 'https://image.tmdb.org/t/p/w500/2cxhvwyEwRlysAmRH4iodkvo0z5.jpg', '+14', 148, 'Accion / Drama / Epico', true, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 5. Wicked
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000005', 'Wicked', 'La historia de como una joven mujer con piel de color verde esmeralda se convierte en la malvada Bruja del Oeste tras forjar una profunda e inesperada amistad con Glinda, la Bruja Buena del Norte.', 'https://image.tmdb.org/t/p/w500/xDGbZ0JJ3mYaGKy4Nzd9Kph6M9L.jpg', 'ATP', 160, 'Fantasia / Musical', true, false, (CURRENT_DATE + INTERVAL '3 days' + TIME '00:00:00'));

-- 6. Moana 2
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000006', 'Moana 2', 'Tras recibir una llamada inesperada de sus ancestros buscadores de caminos, Moana debe viajar a los lejanos mares de Oceania y a aguas peligrosas y perdidas para una aventura sin precedentes junto a Maui.', 'https://image.tmdb.org/t/p/w500/yh64qw9mgXBvlaWDi7Q9tpUBAvH.jpg', 'ATP', 100, 'Animacion / Aventura', true, false, (CURRENT_DATE + INTERVAL '4 days' + TIME '00:00:00'));

-- 7. Alien: Romulus
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000007', 'Alien: Romulus', 'Mientras rebuscan en las profundidades de una estacion espacial abandonada, un grupo de jovenes colonizadores espaciales se encuentra cara a cara con la forma de vida mas aterradora del universo.', 'https://image.tmdb.org/t/p/w500/b33nnKl1GSFbao4l3fZDDqsMx0F.jpg', '+18', 119, 'Terror / Ciencia Ficcion', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 8. Oppenheimer
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000008', 'Oppenheimer', 'Historia del fisico estadounidense J. Robert Oppenheimer, al frente del Laboratorio de Los Alamos durante el Proyecto Manhattan que condujo al desarrollo historico de las primeras armas nucleares.', 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg', '+14', 180, 'Drama / Historia', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 9. Spider-Man: A Traves del Spider-Verso
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000009', 'Spider-Man: A Traves del Spider-Verso', 'Miles Morales es catapultado a traves del Multiverso donde se encuentra con una sociedad de Spider-People encargados de proteger la existencia misma, desafiando que significa ser un heroe.', 'https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg', 'ATP', 140, 'Animacion / Accion', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 10. Joker: Folie a Deux
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000010', 'Joker: Folie a Deux', 'Arthur Fleck se encuentra recluido en Arkham esperando juicio por sus crimenes como Joker. Mientras lidia con su doble identidad, Arthur tropieza con el amor y la musica que siempre ha estado en su interior.', 'https://image.tmdb.org/t/p/w500/aciP8Km0waTLXEYf5ybFK5CSNkM.jpg', '+18', 138, 'Drama / Crimen / Musical', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 11. Beetlejuice Beetlejuice
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000011', 'Beetlejuice Beetlejuice', 'Tres generaciones de la familia Deetz regresan a su hogar en Winter River tras una tragedia. La vida de Lydia da un vuelco cuando su hija rebelde abre accidentalmente el portal al Mas Alla.', 'https://image.tmdb.org/t/p/w500/kKg7jpU3gR4x23w0eW89RzP0Zg7.jpg', '+14', 105, 'Comedia / Fantasia / Terror', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 12. Transformers One
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000012', 'Transformers One', 'La historia no contada del origen de Optimus Prime y Megatron y como pasaron de ser hermanos de armas a convertirse en enemigos acerrimos que cambiaron el destino de Cybertron.', 'https://image.tmdb.org/t/p/w500/qbkPtZWeuvUjV1b09b552N9xL9b.jpg', 'ATP', 104, 'Animacion / Ciencia Ficcion', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 13. Sonic 3: La Pelicula
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000013', 'Sonic 3: La Pelicula', 'Sonic, Knuckles y Tails se unen contra un poderoso nuevo enemigo: Shadow, un misterioso villano con poderes extraordinarios. El Equipo Sonic debera buscar una alianza inesperada.', 'https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg', 'ATP', 110, 'Accion / Aventura / Familiar', true, false, (CURRENT_DATE + INTERVAL '6 days' + TIME '00:00:00'));

-- 14. Avatar: El Sentido del Agua
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000014', 'Avatar: El Sentido del Agua', 'Ambientada mas de una decada despues de los acontecimientos de la primera pelicula, narra la historia de la familia Sully, los problemas que los persiguen y los esfuerzos para mantenerse a salvo.', 'https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg', '+14', 192, 'Ciencia Ficcion / Aventura', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 15. Interstellar
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000015', 'Interstellar', 'Un grupo de cientificos y exploradores emprende el viaje interestelar mas trascendental de la humanidad hacia un agujero de gusano para garantizar la supervivencia de la especie.', 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', '+14', 169, 'Ciencia Ficcion / Drama', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 16. Kung Fu Panda 4
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000016', 'Kung Fu Panda 4', 'Po se prepara para convertirse en el Lider Espiritual del Valle de la Paz, pero antes debe entrenar a un sucesor y enfrentarse a la hechicera Camaleon que resucita a villanos del pasado.', 'https://image.tmdb.org/t/p/w500/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg', 'ATP', 94, 'Animacion / Aventura / Comedia', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- 17. El Conjuro 4: Ultimos Ritos (Preventa)
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000017', 'El Conjuro 4: Ultimos Ritos', 'Los demonologos Ed y Lorraine Warren investigan su caso final mas oscuro y peligroso, enfrentandose a fuerzas malignas que desafian su fe y sus vidas en un enfrentamiento decisivo.', 'https://image.tmdb.org/t/p/w500/wTNr00vW3z3pLwZ9J8fE9mF04.jpg', '+14', 115, 'Terror / Misterio', false, true, '2026-09-15 00:00:00');

-- 18. Superman: Legacy (Preventa)
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000018', 'Superman: Legacy', 'Superman viaja para reconciliar su herencia alienigena con su educacion humana como Clark Kent en Smallville, siendo el faro de esperanza, bondad y justicia para el mundo moderno.', 'https://image.tmdb.org/t/p/w500/8tS3H4uA479H2058xXw4aB71z.jpg', '+14', 135, 'Accion / Superheroes', false, true, '2026-10-01 00:00:00');

-- 19. The Batman: Part II (Preventa)
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000019', 'The Batman: Part II', 'Bruce Wayne continua desentranando la corrupcion sistemica de Ciudad Gotica mientras nuevas mentes criminales emergen desde las sombras para poner a prueba su cruzada justiciera.', 'https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg', '+14', 160, 'Accion / Crimen / Suspenso', false, true, '2026-11-15 00:00:00');

-- 20. El Padrino (Clasico)
INSERT INTO movies (id, title, description, poster_url, classification, duration, genre, is_new_release, is_pre_sale, opening_date) VALUES ('c0000000-0000-0000-0000-000000000020', 'El Padrino', 'Don Vito Corleone, patriarca de una prominente familia criminal en Nueva York, transfiere el mando de su imperio a su hijo menor Michael, marcando el inicio de una sangrienta sucesion familiar.', 'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg', '+18', 175, 'Crimen / Drama / Clasico', false, false, (CURRENT_DATE + INTERVAL '2 days' + TIME '00:00:00'));

-- =============================================================================
-- 9. HORARIOS Y FUNCIONES (SHOW_TIMES) - CARTELERA DESDE HOY 2026-08-20
-- =============================================================================
--Cine 1 (San Miguel) - Sala 1 (2D Atmos): Dune 2 & Gladiador 2
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'b1111111-1111-1111-1111-111111111101', (CURRENT_DATE + INTERVAL '2 days' + TIME '14:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '16:50:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'b1111111-1111-1111-1111-111111111101', (CURRENT_DATE + INTERVAL '2 days' + TIME '17:30:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '20:00:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000003', 'c0000000-0000-0000-0000-000000000004', 'b1111111-1111-1111-1111-111111111101', (CURRENT_DATE + INTERVAL '2 days' + TIME '20:45:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '23:15:00'));

-- Cine 1 (San Miguel) - Sala 2 (3D Digital): IntensaMente 2 & Moana 2 & Sonic 3
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000004', 'c0000000-0000-0000-0000-000000000003', 'b1111111-1111-1111-1111-111111111102', (CURRENT_DATE + INTERVAL '2 days' + TIME '13:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '14:40:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000006', 'b1111111-1111-1111-1111-111111111102', (CURRENT_DATE + INTERVAL '2 days' + TIME '15:30:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '17:15:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000006', 'c0000000-0000-0000-0000-000000000013', 'b1111111-1111-1111-1111-111111111102', (CURRENT_DATE + INTERVAL '2 days' + TIME '18:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '19:55:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000007', 'c0000000-0000-0000-0000-000000000002', 'b1111111-1111-1111-1111-111111111102', (CURRENT_DATE + INTERVAL '2 days' + TIME '20:30:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '22:40:00'));

-- Cine 1 (San Miguel) - Sala 3 (IMAX Laser): Interstellar & Oppenheimer & Dune 2
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000008', 'c0000000-0000-0000-0000-000000000015', 'b1111111-1111-1111-1111-111111111103', (CURRENT_DATE + INTERVAL '2 days' + TIME '14:30:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '17:20:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000009', 'c0000000-0000-0000-0000-000000000008', 'b1111111-1111-1111-1111-111111111103', (CURRENT_DATE + INTERVAL '2 days' + TIME '18:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '21:00:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000010', 'c0000000-0000-0000-0000-000000000001', 'b1111111-1111-1111-1111-111111111103', (CURRENT_DATE + INTERVAL '2 days' + TIME '21:30:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '00:20:00'));

-- Cine 1 (San Miguel) - Sala 4 (4DX Dynamic): Deadpool & Wolverine & Alien: Romulus
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000011', 'c0000000-0000-0000-0000-000000000002', 'b1111111-1111-1111-1111-111111111104', (CURRENT_DATE + INTERVAL '2 days' + TIME '16:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '18:10:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000012', 'c0000000-0000-0000-0000-000000000007', 'b1111111-1111-1111-1111-111111111104', (CURRENT_DATE + INTERVAL '2 days' + TIME '19:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '21:00:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000013', 'c0000000-0000-0000-0000-000000000007', 'b1111111-1111-1111-1111-111111111104', (CURRENT_DATE + INTERVAL '2 days' + TIME '21:45:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '23:45:00'));

-- Cine 2 (Jockey Mall) - Sala 1 (2D Premier): Wicked & Spider-Man & Beetlejuice
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000014', 'c0000000-0000-0000-0000-000000000005', 'b2222222-2222-2222-2222-222222222201', (CURRENT_DATE + INTERVAL '3 days' + TIME '14:00:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '16:45:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000015', 'c0000000-0000-0000-0000-000000000009', 'b2222222-2222-2222-2222-222222222201', (CURRENT_DATE + INTERVAL '3 days' + TIME '17:30:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '19:50:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000016', 'c0000000-0000-0000-0000-000000000011', 'b2222222-2222-2222-2222-222222222201', (CURRENT_DATE + INTERVAL '3 days' + TIME '20:30:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '22:20:00'));

-- Cine 2 (Jockey Mall) - Sala 2 (3D XD): Transformers One & Avatar 2
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000017', 'c0000000-0000-0000-0000-000000000012', 'b2222222-2222-2222-2222-222222222202', (CURRENT_DATE + INTERVAL '2 days' + TIME '15:00:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '16:50:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000018', 'c0000000-0000-0000-0000-000000000014', 'b2222222-2222-2222-2222-222222222202', (CURRENT_DATE + INTERVAL '2 days' + TIME '17:30:00'), (CURRENT_DATE + INTERVAL '2 days' + TIME '20:45:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000019', 'c0000000-0000-0000-0000-000000000014', 'b2222222-2222-2222-2222-222222222202', (CURRENT_DATE + INTERVAL '2 days' + TIME '21:15:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '00:30:00'));

-- Cine 2 (Jockey Mall) - Sala 3 (IMAX GT): Dune 2 & Gladiador 2
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000020', 'c0000000-0000-0000-0000-000000000001', 'b2222222-2222-2222-2222-222222222203', (CURRENT_DATE + INTERVAL '3 days' + TIME '15:30:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '18:20:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000021', 'c0000000-0000-0000-0000-000000000004', 'b2222222-2222-2222-2222-222222222203', (CURRENT_DATE + INTERVAL '3 days' + TIME '19:00:00'), (CURRENT_DATE + INTERVAL '3 days' + TIME '21:30:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000022', 'c0000000-0000-0000-0000-000000000004', 'b2222222-2222-2222-2222-222222222203', (CURRENT_DATE + INTERVAL '3 days' + TIME '22:00:00'), (CURRENT_DATE + INTERVAL '4 days' + TIME '00:30:00'));

-- Cine 2 (Jockey Mall) - Sala 4 (VIP Lounge): Joker 2 & El Padrino & Wicked
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000023', 'c0000000-0000-0000-0000-000000000010', 'b2222222-2222-2222-2222-222222222204', (CURRENT_DATE + INTERVAL '4 days' + TIME '16:00:00'), (CURRENT_DATE + INTERVAL '4 days' + TIME '18:20:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000024', 'c0000000-0000-0000-0000-000000000020', 'b2222222-2222-2222-2222-222222222204', (CURRENT_DATE + INTERVAL '4 days' + TIME '19:00:00'), (CURRENT_DATE + INTERVAL '4 days' + TIME '22:00:00'));
INSERT INTO show_times (id, movie_id, cinema_room_id, start_time, end_time) VALUES ('d0000000-0000-0000-0000-000000000025', 'c0000000-0000-0000-0000-000000000005', 'b2222222-2222-2222-2222-222222222204', (CURRENT_DATE + INTERVAL '4 days' + TIME '22:30:00'), (CURRENT_DATE + INTERVAL '5 days' + TIME '01:10:00'));


SELECT setval(pg_get_serial_sequence('formats','id'), (SELECT MAX(id) FROM formats));

UPDATE show_times st SET format_id=f.id
FROM cinema_rooms cr JOIN cinema_formats cf ON cf.cinema_id=cr.cinema_id JOIN formats f ON f.id=cf.format_id
WHERE cr.id=st.cinema_room_id AND (
 (cr.name ILIKE '%IMAX%' AND f.name ILIKE '%IMAX%') OR
 (cr.name ILIKE '%4DX%' AND f.name ILIKE '%4DX%') OR
 (cr.name ILIKE '%VIP%' AND f.name ILIKE '%VIP%') OR
 (cr.name ILIKE '%3D%' AND f.name ILIKE '%3D%') OR
 (cr.name ILIKE '%2D%' AND f.name ILIKE '%2D%'));

UPDATE show_times st SET end_time=st.start_time + m.duration * INTERVAL '1 minute'
FROM movies m WHERE m.id=st.movie_id;
