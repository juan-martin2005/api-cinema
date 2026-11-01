package com.jg.sistema;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Runs the same behavioral/concurrency suite against the production migrations and database. */
@Testcontainers(disabledWithoutDocker = true)
class PostgresBookingTests extends BookingPaymentTests {
    @org.junit.jupiter.api.Test
    void baselinedLegacySchemaIsUpgradedWithoutLosingData() {
        String schema="legacy_upgrade_test";
        jdbc.execute("CREATE SCHEMA "+schema);
        for(String table:java.util.List.of("roles","users","user_roles","formats","cinemas","cinema_formats","cinema_rooms","seats","movies","show_times","bookings","booking_details","refresh_token","email_verifications")) {
            jdbc.execute("CREATE TABLE "+schema+"."+table+" (LIKE public."+table+" INCLUDING ALL)");
            jdbc.update("INSERT INTO "+schema+"."+table+" SELECT * FROM public."+table);
        }
        jdbc.execute("ALTER TABLE "+schema+".show_times DROP COLUMN format_id");
        jdbc.execute("ALTER TABLE "+schema+".users DROP COLUMN session_version");
        jdbc.execute("ALTER TABLE "+schema+".email_verifications DROP COLUMN attempts, DROP COLUMN sent_at");
        var flyway=org.flywaydb.core.Flyway.configure().dataSource(postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword())
                .schemas(schema).defaultSchema(schema).baselineVersion("1000").locations("classpath:db/migration").load();
        flyway.baseline();flyway.migrate();flyway.validate();
        org.junit.jupiter.api.Assertions.assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM "+schema+".users",Integer.class));
        org.junit.jupiter.api.Assertions.assertEquals(100L,jdbc.queryForObject("SELECT format_id FROM "+schema+".show_times WHERE id=?",Long.class,show));
        org.junit.jupiter.api.Assertions.assertEquals(0L,jdbc.queryForObject("SELECT session_version FROM "+schema+".users WHERE id=?",Long.class,user));
        org.junit.jupiter.api.Assertions.assertNotNull(jdbc.queryForObject("SELECT to_regclass('"+schema+".payment_attempts')::text",String.class));
        org.junit.jupiter.api.Assertions.assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM "+schema+".seat_occupations",Integer.class));
    }
    @org.junit.jupiter.api.Test
    void demoCatalogMigratesIndependentlyWithFutureShowtimes() {
        org.flywaydb.core.Flyway.configure().dataSource(postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword())
                .schemas("demo_seed_test").defaultSchema("demo_seed_test")
                .locations("classpath:db/migration","classpath:db/demo").load().migrate();
        org.junit.jupiter.api.Assertions.assertTrue(jdbc.queryForObject("SELECT COUNT(*) FROM demo_seed_test.show_times WHERE start_time>CURRENT_TIMESTAMP",Integer.class)>0);
        org.junit.jupiter.api.Assertions.assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM demo_seed_test.show_times WHERE format_id IS NULL",Integer.class));
        org.junit.jupiter.api.Assertions.assertTrue(jdbc.queryForObject("INSERT INTO demo_seed_test.formats(name,price) VALUES ('New format',20) RETURNING id",Long.class)>5);
    }
    @Container static final PostgreSQLContainer postgres=new PostgreSQLContainer("postgres:17-alpine");
    @Container static final org.testcontainers.containers.GenericContainer<?> redis=
            new org.testcontainers.containers.GenericContainer<>(org.testcontainers.utility.DockerImageName.parse("redis:7-alpine")).withExposedPorts(6379);
    @DynamicPropertySource static void database(DynamicPropertyRegistry properties) {
        properties.add("spring.datasource.url",postgres::getJdbcUrl);
        properties.add("spring.datasource.username",postgres::getUsername);
        properties.add("spring.datasource.password",postgres::getPassword);
        properties.add("spring.datasource.driver-class-name",()->"org.postgresql.Driver");
        properties.add("spring.jpa.database-platform",()->"org.hibernate.dialect.PostgreSQLDialect");
        properties.add("spring.flyway.enabled",()->true);
        properties.add("spring.sql.init.mode",()->"never");
        properties.add("spring.cache.type",()->"redis");
        properties.add("spring.data.redis.host",redis::getHost);
        properties.add("spring.data.redis.port",()->redis.getMappedPort(6379));
    }
}
