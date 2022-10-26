package com.study.yongyeon.sunday.spring.springsunday.security.service;

import com.study.yongyeon.sunday.spring.springsunday.dto.ClubAuthMemberDTO;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private PasswordEncoder passwordEncoder;

    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("AuthenticationSuccessHandler :: onAuthenticationSuccess :: start");

        ClubAuthMemberDTO authMember = (ClubAuthMemberDTO) authentication.getPrincipal();
        boolean isFromLocal = ClubMemberSocial.LOCAL.equals(authMember.getFromSocial());

        log.info("Need Modify Member? : {}",isFromLocal);

        boolean isDefaultPassword = passwordEncoder.matches("1111",authMember.getPassword());

        //패스워드가 1111 이면서 LOCAL이 아닌 경우
        if (isDefaultPassword && ! isFromLocal ){
            redirectStrategy.sendRedirect(request, response, "/member/modify?from="+authMember.getFromSocial());
        }

    }
}
