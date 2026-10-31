package com.jg.sistema.shared.infrastructure.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures the Redis cache manager with polymorphic type validation.
     * The polymorphic type validator restricts deserialization to trusted
     * application and Java types. Application classes under com.jg.sistema
     * are explicitly allowed, along with java.util collections and
     * java.time types required by the cached objects.
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        PolymorphicTypeValidator polymorphicTypeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType("com.jg.sistema")
                .allowIfSubType("java.util")
                .allowIfSubType("java.time")
                .build();


        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(GenericJacksonJsonRedisSerializer
                                        .builder()
                                        .enableDefaultTyping(polymorphicTypeValidator)
                                        .build())
                );

        RedisCacheConfiguration seatLock = defaultConfig
                .entryTtl(Duration.ofMinutes(5));


        Map<String, RedisCacheConfiguration> cacheConfig =
                Map.of(
                        "seat", seatLock
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfig)
                .build();
    }
}
