package com.example.authservice.kafka;

import com.example.authservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService
{
    private final KafkaTemplate<String,String> template;
    public void sendMessage(Object object) // TODO: Topic management
    {
        template.send("emailTopic", JsonMapper.convertToJsonString(object));
    }
}
