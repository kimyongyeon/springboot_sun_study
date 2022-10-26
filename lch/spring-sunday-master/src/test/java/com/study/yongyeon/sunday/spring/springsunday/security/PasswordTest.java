package com.study.yongyeon.sunday.spring.springsunday.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 인코딩_테스트(){
        String Password = "test";
        String enPw = passwordEncoder.encode(Password);
        System.out.println("암호화된 패스워드는 ::::" + enPw);

        boolean matchResult = passwordEncoder.matches(Password, enPw);

        System.out.println("is Match??? ::::" + matchResult);
    }
}
