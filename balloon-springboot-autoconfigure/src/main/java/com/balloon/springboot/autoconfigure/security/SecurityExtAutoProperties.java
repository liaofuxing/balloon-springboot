package com.balloon.springboot.autoconfigure.security;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.SECURITY)
public class SecurityExtAutoProperties {

    /**
     * 是否启用配置
     */
    private boolean enabled;

    /**
     * 是否允许跨域
     */
    private boolean cors;

    /**
     * Security 默认过滤url
     */
    private String filterUrl;
}
