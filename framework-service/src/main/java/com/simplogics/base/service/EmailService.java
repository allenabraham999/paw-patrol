package com.simplogics.base.service;

import com.simplogics.base.exception.PawException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService implements IEmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.from}")
    private String FROM_EMAIL_ADDRESS;

    @Override
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) throws PawException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariables(variables);
            String htmlBody = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setFrom(FROM_EMAIL_ADDRESS);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new PawException("forget.password.email.fail", HttpStatus.BAD_REQUEST);
        }
    }
}
