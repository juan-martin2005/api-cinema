package com.jg.sistema;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Runs the same behavioral/concurrency suite against the production migrations and database. */
@Testcontainers(disabledWithoutDocker = true)
class PostgresBookingTests extends BookingPaymentTests {
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
