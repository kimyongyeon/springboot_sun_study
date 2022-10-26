package com.study.yongyeon.sunday.spring.springsunday.security;

import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMember;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberRole;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import com.study.yongyeon.sunday.spring.springsunday.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTest {
    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 클럽멤버_더미_생성(){
        //given
        /*
            1-80까지는     User만 지정
            81-90까지는    User,Manager
            91-100까지는   User,Manager,Admin
         */

        IntStream.rangeClosed(1,100)
                .forEach(i -> {
                    ClubMember clubMember = ClubMember.builder()
                            .email("user" + i + "@test.com")
                            .name("User"+i)
                            .fromSocial(ClubMemberSocial.LOCAL)
                            .password(passwordEncoder.encode("1111"))
                            .build();
                    //default Role
                    clubMember.addMemberRole(ClubMemberRole.USER);
                    if (i > 80) { clubMember.addMemberRole(ClubMemberRole.MANAGER); }
                    if (i > 90) { clubMember.addMemberRole(ClubMemberRole.ADMIN); }
                    clubMemberRepository.save(clubMember);
                });
    }

    @Test
    public void 클럽멤버_읽기_테스트(){
        final String hasRoleAdmin = "user" + 95 + "@test.com";

        Optional<ClubMember> adminMember = clubMemberRepository.findByEmail(hasRoleAdmin, ClubMemberSocial.LOCAL);
        System.out.println(adminMember.get());
    }
}
