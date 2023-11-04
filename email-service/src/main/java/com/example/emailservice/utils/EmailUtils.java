package com.example.emailservice.utils;

public class EmailUtils
{
    public static String getEmailMessage(String host, String token)
    {
        return "Hello " + "\n\n" + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) //TODO: Path management
    {
        return host + "/api/v1/auth/enable/" + token;
    }
}
