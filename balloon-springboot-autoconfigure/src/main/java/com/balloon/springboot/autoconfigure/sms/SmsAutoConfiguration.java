package com.balloon.springboot.autoconfigure.sms;


import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.util.List;


/**
 * 短信配置
 *
 * @author liaofuxing
 * @since
 */
@SuppressWarnings("all")
@Configuration
@EnableConfigurationProperties(value = SmsAutoProperties.class)
@ConditionalOnProperty(prefix = AutoConfigConstant.SMS, name = AutoConfigConstant.ENABLED, havingValue = AutoConfigConstant.TRUE)
public class SmsAutoConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(SmsAutoConfiguration.class);
//    private final SmsProperties properties;
//
//    public SmsAutoConfiguration(SmsProperties properties) {
//        this.properties = properties;
//    }

    @Bean
    public SmsTemplate smsTemplate() {
        SmsTemplate smsTemplate = new SmsTemplate();
        configure(smsTemplate);
        return smsTemplate;
    }

    /**
     * 配置短信模板
     *
     * @param smsTemplate 短信模板
     */
    private void configure(SmsTemplate smsTemplate) {
        smsTemplate.test();
//        Map<String, SmsFieldProperties> smsHandlerMap = properties.getBeanMap();
        logger.info("configure");
    }

    /**
     * 短信模板处理配置
     */
    @Configuration
    class SmsTemplatePostProcessingConfiguration {

        private final SmsTemplate smsTemplate;

        private final List<SmsSendInterceptor> smsSendInterceptors;

        public SmsTemplatePostProcessingConfiguration(SmsTemplate smsTemplate, List<SmsSendInterceptor> smsSendInterceptors) {
            this.smsTemplate = smsTemplate;
            smsTemplate.test();
            this.smsSendInterceptors = smsSendInterceptors;
        }

        /**
         * 添加拦截器
         */
        @PostConstruct
        public void init() {
            logger.info("init 被加载");
        }
    }
}
