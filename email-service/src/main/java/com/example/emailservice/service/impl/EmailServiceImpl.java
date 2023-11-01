package com.example.emailservice.service.impl;

import com.example.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.emailservice.utils.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService
{
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Override
    @Async
    public void sendBasicEmailMessage(String receiverEmail, String token)
    {
        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Accounted listed");
            message.setFrom(emailSender);
            message.setTo(receiverEmail);
            message.setText(getEmailMessage(host, token));
            javaMailSender.send(message);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException("Couldn't send basic message");
        }
    }
}
