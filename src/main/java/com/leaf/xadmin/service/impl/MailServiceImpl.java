package com.leaf.xadmin.service.impl;

import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-02-06 18:03</p>
 * <p>version: 1.0</p>
 */
@Service
@Slf4j
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MailSendException(ErrorStatus.MAIL_SEND_ERROR.getMessage());
        }

    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(ErrorStatus.MAIL_SEND_ERROR.getMessage());
        }
    }

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
        } catch (Exception e) {
            throw new MailSendException(ErrorStatus.MAIL_SEND_ERROR.getMessage());
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(ErrorStatus.MAIL_SEND_ERROR.getMessage());
        }
    }

    @Override
    public void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> templateParams) {
        Context context = new Context();
        Iterator<Map.Entry<String, Object>> iterator = templateParams.entrySet().iterator();
        // 设置模板相关属性
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String emailContent = templateEngine.process(templateName, context);

        sendHtmlMail(to, subject, emailContent);
    }
}
