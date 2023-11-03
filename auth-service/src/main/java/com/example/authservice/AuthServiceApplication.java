package com.example.authservice;

import com.example.authservice.confirmation.ConfirmationMessage;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.user.Role;
import com.example.authservice.user.User;
import com.example.authservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthServiceApplication
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static void main(String[] args)
    {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner() //TODO: Revoke existing tokens on the launch
//    {
//        return args ->
//        {
//            userRepository.save
//                    (
//                            User.builder()
//                                    .username("admin")
//                                    .password(passwordEncoder.encode("admin"))
//                                    .email("Rhea.Bode21@yahoo.com")
//                                    .role(Role.ADMIN)
//                                    .build()
//                    );
//        };
//    }

//    @Bean
//    CommandLineRunner commandLineRunner(KafkaTemplate<String,String> template)
//    {
//        return args ->
//        {
//            for (int i = 0; i < 2; i++)
//            {
//                template.send("emailTopic", JsonMapper.convertToJsonString(ConfirmationMessage.builder().email("DASD").token("1234").build()));
//            }
//        };
//    }
}
