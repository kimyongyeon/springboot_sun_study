package com.study.yongyeon.sunday.spring.springsunday.dto;

import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {
    private String email;
    private String name;
    private ClubMemberSocial fromSocial;
    private Map<String, Object> attr;

    public ClubAuthMemberDTO(
            String username,
            String password,
            ClubMemberSocial fromSocial,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
        this.email = username;
        this.fromSocial = fromSocial;
    }
    public ClubAuthMemberDTO(
            String username,
            String password,
            ClubMemberSocial fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr
    ) {
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
