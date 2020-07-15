package com.balloon.springboot.autoconfigure.security;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.SECURITY)
public class SecurityExtAutoProperties {

    private boolean enabled;
}
