package com.balloon.springboot.mail.service.impl;

import com.balloon.springboot.mail.MailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    /**
     * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
     */
    @Setter
    private JavaMailSender mailSender;

    @Setter
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 发送人,配置文件中我的qq邮箱
     */
    @Value("${balloon.mail.from}")
    private String from;

    /**
     * 收件人,邮件发送给谁
     */
    @Value("${balloon.mail.to}")
    private String to;

    @Value("${balloon.mail.subject}")
    private String subject;

    /**
     * 简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        logger.info("将邮件发送给:{}", to);
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * html邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        logger.info("将邮件发送给:{}", to);
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人
            messageHelper.setTo(subject);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            logger.info("邮件已经发送");
        } catch (MessagingException e) {
            logger.error("发送邮件时发生异常！", e);
        }
    }

    @Override
    public void sendTemplateFreemarkerMail(String to, String subject, Map<String, Object> model, String templateFile) {
        logger.info("将邮件发送给:{}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateFile);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            messageHelper.setText(html, true);
            mailSender.send(message);
        }catch (MessagingException | TemplateException | IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            logger.info("邮件已经发送");
        } catch (MessagingException e) {
            logger.error("发送邮件时发生异常！", e);
        }

    }

    @Override
    public void sendBatchTemplateFreemarkerMail(Map<String, Object> model, String templateFile) {
        String[] split = this.to.split(",");
        for (String to: split) {
            sendTemplateFreemarkerMail(to, this.subject, model,templateFile);
        }
    }
}
