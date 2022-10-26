package com.study.yongyeon.sunday.spring.springsunday.controller;

import com.study.yongyeon.sunday.spring.springsunday.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/permit")
public class LoginController {
    private final HttpSession httpSession;

    @GetMapping("/all")
    public String exAll(){
        //(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
//        log.info("Member is ::: {}",clubAuthMemberDTO);
        return "/login/all";
    }

    @GetMapping("/member")
    public String exMember(){
        //@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
//        log.info("Member is ::: {}",clubAuthMemberDTO);

        log.info("exMember......");
        return "/login/member";
    }

    @GetMapping("/admin")
    public String exAdmin(){
        //@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
//        log.info("Member is ::: {}",clubAuthMemberDTO);

        log.info("exAdmin......");
        return "/login/admin";
    }
}
