package com.balloon.springboot.autoconfigure.redis;


import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.redis.utils.RedisUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import io.lettuce.core.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * redis 工具自动装载
 *
 * @author liaofuxing
 *
 */
@SuppressWarnings("all")
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(value = RedisAutoProperties.class)
@ConditionalOnProperty(prefix = AutoConfigConstant.REDIS, name = AutoConfigConstant.ENABLED, havingValue = AutoConfigConstant.TRUE)
public class RedisUtilsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtilsAutoConfiguration.class);

    @Bean
    @ConditionalOnClass(RedisClient.class)
    @ConditionalOnBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        lettuceConnectionFactory.setShareNativeConnection(false);
        RedisTemplate redisTemplate = new RedisTemplate<>();

        // 使用 jackson 序列化工具
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        JacksonObjectMapper mapper = new JacksonObjectMapper();

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        mapper.enableDefaultTyping(JacksonObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(serializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(serializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnClass(RedisClient.class)
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public RedisUtils redisUtils(StringRedisTemplate stringRedisTemplate) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setStringRedisTemplate(stringRedisTemplate);
        return redisUtils;
    }

//    @Bean
//    @ConditionalOnBean(StringRedisTemplate.class)
//    public DistributedLockHandler distributedLockHandler(StringRedisTemplate stringRedisTemplate) {
//        DistributedLockHandler distributedLockHandler = new DistributedLockHandler();
//        distributedLockHandler.setTemplate(stringRedisTemplate);
//        return distributedLockHandler;
//    }

    // 分布式 redis 锁, spring-integration-redis
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory){
        return new RedisLockRegistry(redisConnectionFactory, "redis-lock");
    }

    // redis 缓存
    @EnableCaching
    @ConditionalOnProperty(prefix = AutoConfigConstant.REDIS, name = AutoConfigConstant.ENABLED_CACHE, havingValue = AutoConfigConstant.TRUE)
    public class RedisCacheConfig extends CachingConfigurerSupport {

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时
            return RedisCacheManager
                    .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                    .cacheDefaults(redisCacheConfiguration).build();
        }

        /**
         * 自动装载初始化拦截器
         */
        @PostConstruct
        public void init() {
            logger.info("Reids 缓存已启用");
        }

    }



    /**
     * 自动装载初始化拦截器
     */
    @PostConstruct
    public void init() {
        logger.info("RedisUtilsAutoConfiguration 已被自动装载");
    }

}
