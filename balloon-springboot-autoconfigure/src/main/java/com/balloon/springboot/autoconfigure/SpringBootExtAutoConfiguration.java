package com.balloon.springboot.autoconfigure;

import com.balloon.core.id.GenerateId;
import com.balloon.core.id.UUIDGenerateId;
import com.balloon.springboot.autoconfigure.mail.MailAutoConfiguration;
import com.balloon.springboot.autoconfigure.redis.RedisUtilsAutoConfiguration;
import com.balloon.springboot.autoconfigure.security.SecurityExtAutoConfiguration;
import com.balloon.springboot.autoconfigure.sms.SmsAutoConfiguration;
import com.balloon.springboot.core.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * springboot 自动装载
 *
 * @author liaofuxing
 */
@Configuration
@EnableConfigurationProperties(value = SpringBootExtProperties.class)
@Import({RedisUtilsAutoConfiguration.class,
        SecurityExtAutoConfiguration.class,
        SmsAutoConfiguration.class,
        MailAutoConfiguration.class})
@ConditionalOnProperty(prefix = AutoConfigConstant.SPRINGBOOT, name = AutoConfigConstant.ENABLED, havingValue = AutoConfigConstant.TRUE)
public class SpringBootExtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootExtAutoConfiguration.class);

    /**
     * springContextHolder
     *
     * @return springBeanUtils
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringContextHolder contextHolder() {
        logger.info("SpringContextHolder 已被自动装载");
        return new SpringContextHolder();
    }

    /**
     * create generator id.
     * default used uuid
     *
     * @return GenerateId
     */
    @Bean
    @ConditionalOnMissingBean
    public GenerateId<String> generatorId() {
        return new UUIDGenerateId();
    }
}