package com.balloon.springboot.autoconfigure.sms;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.SMS)
public class SmsAutoProperties {

    private boolean enabled;
}
