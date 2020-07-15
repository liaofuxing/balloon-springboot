package com.balloon.springboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * springboot 配置
 * @author liaofuxing
 */
@Data
@ConfigurationProperties(prefix = AutoConfigConstant.SPRINGBOOT)
public class SpringBootExtProperties {

    private boolean enabled;

}