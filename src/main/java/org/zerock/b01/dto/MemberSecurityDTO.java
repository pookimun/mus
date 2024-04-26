package org.zerock.b01.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User {
    private String m_id;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;

    public MemberSecurityDTO(String username, String password, String email, boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.m_id = username;
        this.m_pw = password;
        this.m_email = email;
        this.m_del = del;
        this.m_social = social;
    }

}
