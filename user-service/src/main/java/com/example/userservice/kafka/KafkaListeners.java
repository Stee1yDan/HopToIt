package com.example.userservice.kafka;


import com.example.userservice.service.UserService;
import com.example.userservice.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners
{
    private final UserService userService;
    @KafkaListener(topics = "registerTopic", groupId = "registrationId")
    void listener(String data)
    {
        String message = (String) JsonMapper.convertFromJsonString(data, String.class);
        userService.registerUser(message);
    }
}
