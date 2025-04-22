package com.example.favoritethings.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public void sendConfirmationEmail(String toEmail, String token) {
        String confirmationUrl =  baseUrl + "/api/auth/confirm?token=" + token;
        String subject = "Подтверждение Email";
        String htmlMessage = "<html>" +
                            "  <body>" +
                            "    <p>Чтобы подтвердить email, нажмите на ссылку ниже:</p>" +
                            "    <p><a href=\"" + confirmationUrl + "\">Подтвердить email</a></p>" +
                            "  </body>" +
                            "</html>";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true);

            javaMailSender.send(mimeMessage);
            logger.info("Письмо с подтверждением отправлено на: {}", toEmail);
        } catch (MessagingException | MailException e) {
            logger.error("Ошибка при отправке письма на {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить письмо подтверждения: " + e.getMessage(), e);
        }
    }
}