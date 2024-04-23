package org.zerock.b01.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String m_id;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;
    private String username;

    private Map<String, Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(String username, String password, String m_email, boolean m_del, boolean m_social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.m_id = username;
        this.m_pw = password;
        this.m_email = m_email;
        this.m_del = m_del;
        this.m_social = m_social;
        this.username = username;

    }

    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.m_id;
    }

    public String getUsername() {
        return this.m_id;
    }

}