package com.page.pulse.orchestrator.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Centralizes cache-related beans so Redis caches follow a consistent setup.
 *
 * @author lewisjones
 */
@Configuration
public class CacheConfiguration
{
    /**
     * Configures Redis cache settings based on application properties.
     *
     * @param cacheProperties the cache properties
     * @return the Redis cache configuration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration( final CacheProperties cacheProperties )
    {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule( new JavaTimeModule() )
            .setSerializationInclusion( JsonInclude.Include.NON_NULL )
            .findAndRegisterModules();

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith( RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer( objectMapper ) ) );

        final CacheProperties.Redis redis = cacheProperties.getRedis();
        if ( redis.getTimeToLive() != null )
        {
            configuration = configuration.entryTtl( redis.getTimeToLive() );
        }
        if ( redis.getKeyPrefix() != null )
        {
            configuration = configuration.prefixCacheNameWith( redis.getKeyPrefix() );
        }
        if ( !redis.isCacheNullValues() )
        {
            configuration = configuration.disableCachingNullValues();
        }
        if ( !redis.isUseKeyPrefix() )
        {
            configuration = configuration.disableKeyPrefix();
        }
        return configuration;
    }

}
