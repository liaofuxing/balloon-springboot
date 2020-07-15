package com.balloon.springboot.autoconfigure.redis;


import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import com.balloon.springboot.redis.lock.DistributedLockHandler;
import com.balloon.springboot.redis.utils.RedisUtils;
import io.lettuce.core.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

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
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
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

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public DistributedLockHandler distributedLockHandler(StringRedisTemplate stringRedisTemplate) {
        DistributedLockHandler distributedLockHandler = new DistributedLockHandler();
        distributedLockHandler.setTemplate(stringRedisTemplate);
        return distributedLockHandler;
    }

    /**
     * 自动装载初始化拦截器
     */
    @PostConstruct
    public void init() {
        logger.info("RedisUtilsAutoConfiguration 已被自动装载");
    }

}
