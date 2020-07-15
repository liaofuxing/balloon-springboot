package com.balloon.springboot.mail;

import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;


public interface MailService {


    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
     void sendHtmlMail(String to, String subject, String content);

     void sendTemplateFreemarkerMail(String to, String subject, Map<String, Object> model, String templateFile);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
     void sendAttachmentsMail(String to, String subject, String content, String filePath);

     void sendBatchTemplateFreemarkerMail(Map<String, Object> model, String templateFile);

}