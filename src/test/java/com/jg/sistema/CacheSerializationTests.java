package com.jg.sistema;

import com.jg.sistema.shared.infrastructure.config.CacheConfig;
import com.jg.sistema.movie.domain.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import java.util.UUID;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CacheSerializationTests {
    @Test void configuredRedisSerializerRoundTripsMovieAndDates() {
        var config=new CacheConfig();
        var serializer=config.redisDefaults().getValueSerializationPair();
        var movie=new Movie(UUID.randomUUID(),"Film","Description","url","PG",120,"Drama",true,false,LocalDateTime.of(2026,11,1,12,0));
        assertEquals(movie,serializer.read(serializer.write(movie)));
        assertDoesNotThrow(()->config.errorHandler().handleCacheGetError(new RuntimeException("Redis unavailable"),new org.springframework.cache.concurrent.ConcurrentMapCache("test"),movie.id()));
    }
}
