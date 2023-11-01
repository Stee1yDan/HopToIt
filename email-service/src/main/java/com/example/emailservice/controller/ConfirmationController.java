package com.example.emailservice.controller;

import com.example.emailservice.model.ConfirmationRequest;
import com.example.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConfirmationController
{
    private final EmailService emailService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendEmail(@RequestBody ConfirmationRequest request) //TODO: Validate token?
    {
        emailService.sendBasicEmailMessage(request.getEmail(),request.getToken());
    }
}
