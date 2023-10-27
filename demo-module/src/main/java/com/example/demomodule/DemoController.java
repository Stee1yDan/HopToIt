package com.example.demomodule;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController
{
    @GetMapping("/get")
    public String get()
    {
        return "Hello from here";
    }
}
