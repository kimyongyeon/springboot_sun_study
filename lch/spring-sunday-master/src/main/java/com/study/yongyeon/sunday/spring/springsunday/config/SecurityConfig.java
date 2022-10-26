package com.study.yongyeon.sunday.spring.springsunday.config;

import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubLoginFailureHandler;
import com.study.yongyeon.sunday.spring.springsunday.security.service.ClubLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
//    private final ClubUserDetailService clubUserDetailService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests(auth->
                    auth    .antMatchers("/permit/all").permitAll()
                            .antMatchers("/permit/member").hasRole("USER")
                            .antMatchers("/permit/admin").hasRole("ADMIN"))

//                .userDetailsService(clubUserDetailService)
                .formLogin()
//                .successHandler(clubMemberSuccessHandler())
//                .failureHandler(authenticationFailureHandler())
                .and().oauth2Login()
                .and().formLogin()
                .and()
                .csrf().disable()
                .logout();
//                .and()
//                .rememberMe()
//                .tokenValiditySeconds(60*60*24*7);

        return http.build();
    }

    @Bean
    public ClubLoginSuccessHandler clubMemberSuccessHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new ClubLoginFailureHandler();
    }
}
