package com.example.stockinfoservice.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class ClientConfig {
    @Bean
    public static Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 100000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 100000);

        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }
}
