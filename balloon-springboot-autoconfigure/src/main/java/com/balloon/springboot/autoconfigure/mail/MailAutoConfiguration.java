package com.balloon.springboot.autoconfigure.mail;


import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import com.balloon.springboot.mail.service.impl.MailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

/**
 * 简单邮件发送工具自动装载
 *
 * @author liaofuxing
 */
@SuppressWarnings("all")
@Configuration
@EnableConfigurationProperties(value = MailAutoProperties.class)
@ConditionalOnProperty(prefix = AutoConfigConstant.MAIL, name = AutoConfigConstant.ENABLED, havingValue = AutoConfigConstant.TRUE)
public class MailAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MailAutoConfiguration.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Bean
    @ConditionalOnMissingBean
    public MailServiceImpl mailServiceImpl() {
        MailServiceImpl mailServiceImpl = new MailServiceImpl();
        configure(mailServiceImpl);
        return mailServiceImpl;
    }


    /**
     * 配置短信模板
     *
     * @param mailServiceImpl 邮件发送接口
     */
    private void configure(MailServiceImpl mailServiceImpl) {
        mailServiceImpl.setMailSender(mailSender);
        mailServiceImpl.setFreeMarkerConfigurer(freeMarkerConfigurer);
    }

    /**
     * 添加拦截器
     */
    @PostConstruct
    public void init() {
        logger.info("mailAutoConfiguration 已被自动装载");
    }

}
