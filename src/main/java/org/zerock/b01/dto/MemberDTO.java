package org.zerock.b01.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String mid;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;

    private String m_join_path; //가입 경로
    private boolean m_optional_terms; //선택 약관 동의
    private int m_point; //적립금

    private Map<String, Object> props; //소셜 로그인 정보
}
