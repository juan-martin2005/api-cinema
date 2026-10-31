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
public class CacheConfig implements org.springframework.cache.annotation.CachingConfigurer {
    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name="spring.cache.type",havingValue="simple")
    public org.springframework.cache.CacheManager localCacheManager() {
        return new org.springframework.cache.concurrent.ConcurrentMapCacheManager();
    }
    @Override public org.springframework.cache.interceptor.CacheErrorHandler errorHandler() {
        return new org.springframework.cache.interceptor.CacheErrorHandler() {
            private void warn(RuntimeException ex) { org.slf4j.LoggerFactory.getLogger(CacheConfig.class).warn("Cache unavailable; using database ({})", ex.getClass().getSimpleName()); }
            public void handleCacheGetError(RuntimeException ex,org.springframework.cache.Cache c,Object key) { warn(ex); }
            public void handleCachePutError(RuntimeException ex,org.springframework.cache.Cache c,Object key,Object value) { warn(ex); }
            public void handleCacheEvictError(RuntimeException ex,org.springframework.cache.Cache c,Object key) { warn(ex); }
            public void handleCacheClearError(RuntimeException ex,org.springframework.cache.Cache c) { warn(ex); }
        };
    }

    /**
     * Configures the Redis cache manager with polymorphic type validation.
     * The polymorphic type validator restricts deserialization to trusted
     * application and Java types. Application classes under com.jg.sistema
     * are explicitly allowed, along with java.util collections and
     * java.time types required by the cached objects.
     */
    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name="spring.cache.type",havingValue="redis",matchIfMissing=true)
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        return RedisCacheManager.builder(connectionFactory)
                .transactionAware()
                .cacheDefaults(redisDefaults())
                .build();
    }

    public RedisCacheConfiguration redisDefaults() {

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

        return defaultConfig;
    }
}
