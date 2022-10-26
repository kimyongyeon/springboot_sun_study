package com.study.yongyeon.sunday.spring.springsunday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableJpaAuditing
@EnableWebSecurity
@SpringBootApplication
public class SpringSundayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSundayApplication.class, args);
    }
}
