package com.example.authservice.interfaces;

public interface IMessageService
{
    void sendEmailConfirmationMessage(Object object);

    void sendRegistrationMessage(Object object);
}
