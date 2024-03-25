package com.example.authservice.kafka;

import com.example.authservice.interfaces.IMessageService;
import com.example.authservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService implements IMessageService
{
    private final KafkaTemplate<String,String> template;

    @Override
    public void sendEmailConfirmationMessage(Object object)
    {
        template.send("emailTopic", JsonMapper.convertToJsonString(object));
    }

    @Override
    public void sendRegistrationMessage(Object object)
    {
        template.send("registerTopic", JsonMapper.convertToJsonString(object));
    }
}
