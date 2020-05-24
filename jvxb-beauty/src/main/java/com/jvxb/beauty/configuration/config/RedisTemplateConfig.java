package com.jvxb.beauty.configuration.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis序列化配置，Object序列化为Json，比自带的jdk序列化方式，具有更快的速度
 * 这个主要是根据redis存储的数据类型需求决定，key一般都是String，但是value可能不一样，一般有两种，String和 Object；
 * 如果k-v都是String类型，我们可以直接用 StringRedisTemplate，这个是官方建议的，也是最方便的，直接导入即用，无需多余配置！
 * 如果k-v是Object类型，则需要自定义 RedisTemplate
 *
 * @author lcl
 *
 * @since 2019-09-10
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * 代码使用方式：直接引入
     *
     * @Autowired RedisTemplate<String, Object> redisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化规则
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // 缓存管理器
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .computePrefixWith(name -> name + ":");
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory).cacheDefaults(defaultCacheConfiguration);
        return builder.build();
    }
}
