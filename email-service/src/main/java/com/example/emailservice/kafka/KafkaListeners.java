package com.example.emailservice.kafka;

import com.example.emailservice.confirmation.ConfirmationMessage;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners
{
    private final EmailService emailService;
    @KafkaListener(topics = "emailTopic", groupId = "emailId")
    void listener(String data)
    {
        ConfirmationMessage message = (ConfirmationMessage) JsonMapper.convertFromJsonString(data, ConfirmationMessage.class);
        emailService.sendBasicEmailMessage(message.getEmail(), message.getToken());
    }
}
