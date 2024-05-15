package org.zerock.b01.dto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.zerock.b01.constant.Role;

@Data
public class MemberJoinDTO {
    private String mid;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String m_pw;

    @Email
    @NotBlank(message = "이메일을 입력해 주세요")
    private String m_email;
    private boolean m_del;
    private boolean m_social;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String m_join_path; //가입 경로
    private boolean m_optional_terms; //선택 약관 동의
    private int m_point = 50000; //가입 적립금 5만원
}
