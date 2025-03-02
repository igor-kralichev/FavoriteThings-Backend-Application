package com.example.favoritethings.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки писем (подтверждение регистрации).
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.from}")
    private String mailFrom;  // Адрес отправителя

     /**
     * Сервис для отправки письма с подтверждением регистрации.
     * @param toEmail email получателя
     * @param token токен для подтверждения
     */
    public void sendConfirmationEmail(String toEmail, String token) {
        // Формирование URL для подтверждения email (измените при необходимости)
        String confirmationUrl = "http://localhost:8090/api/auth/confirm?token=" + token;
        String subject = "Подтверждение Email";
        String message = "Для подтверждения email перейдите по ссылке: " + confirmationUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom); // Устанавливаем адрес отправителя
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            // Попытка отправки письма
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            // Логирование ошибки или дополнительная обработка
            System.out.println("Ошибка при отправке письма: " + e.getMessage());
        }
    }
}
