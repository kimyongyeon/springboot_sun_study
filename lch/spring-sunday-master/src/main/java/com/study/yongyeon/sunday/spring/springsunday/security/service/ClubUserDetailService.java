package com.study.yongyeon.sunday.spring.springsunday.security.service;

import com.study.yongyeon.sunday.spring.springsunday.dto.ClubAuthMemberDTO;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMember;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import com.study.yongyeon.sunday.spring.springsunday.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubUserDetailService implements UserDetailsService {
    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("ClubUserDetailService::loadUserByUsername ::: {}",username);
        Optional<ClubMember> findMember = clubMemberRepository.findByEmail(username, ClubMemberSocial.LOCAL);

        log.info("ClubUserDetailService::findMember ::: {}",findMember);

        if(! findMember.isPresent()) {
            throw new UsernameNotFoundException("Check Email or Social");
        } else {
            log.info("isNotPresent");
        }

        ClubMember clubMember = findMember.get();

        log.info("---------------------\n{}",clubMember);
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getName(),
                clubMember.getPassword(),
                clubMember.getFromSocial(),
                clubMember.getRoleSet().stream()
                        .map(role ->new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet())
        );
        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.getFromSocial());


        log.info("ClubUserDetailService::clubAuthMember ::: {}",clubAuthMember);

        return clubAuthMember;
    }

}
