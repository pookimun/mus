package org.zerock.b01.dto;
import lombok.Data;

@Data
public class MemberJoinDTO {
    private String mid;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;

    private String m_join_path; //가입 경로
    private boolean m_optional_terms; //선택 약관 동의
    private int m_point = 50000; //가입 적립금 5만원
}
