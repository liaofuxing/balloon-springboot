package com.balloon.springboot.autoconfigure.mail;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.MAIL)
public class MailAutoProperties {

    private boolean enabled;

    /**
     * 邮件发送人邮箱
     */
    private String from;

    /**
     * 邮件接收人邮箱, 可以是多个邮箱, 用 "," 隔开
     */
    private String to;

    /**
     * 邮件主题
     */
    private String subject;
}
