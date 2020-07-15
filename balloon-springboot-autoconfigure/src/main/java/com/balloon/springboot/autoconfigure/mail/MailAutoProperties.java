package com.balloon.springboot.autoconfigure.mail;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.MAIL)
public class MailAutoProperties {

    private boolean enabled;

    private String from;

    private String to;

    private String subject;
}
