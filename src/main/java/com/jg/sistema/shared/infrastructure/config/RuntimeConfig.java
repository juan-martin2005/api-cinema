package com.jg.sistema.shared.infrastructure.config;
import java.time.*;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
@Configuration @EnableScheduling
public class RuntimeConfig {
    @Bean public Clock clock(@Value("${app.zone:America/Lima}") String zone) { return Clock.system(ZoneId.of(zone)); }
}
