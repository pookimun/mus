package org.zerock.b01.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.dto.MemberDTO;

public interface MemberService {
    static class midExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO)throws midExistException;
    void edit(MemberDTO memberDTO);
    /*boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder);*/

    MemberDTO readMember(String mid);

    default Member dtoToEntity(MemberDTO memberDTO) {
        Member member = Member.builder()
                .mid(memberDTO.getMid())
                .m_pw(memberDTO.getM_pw())
                .m_email(memberDTO.getM_email())
                .m_del(memberDTO.isM_del())
                .m_social(memberDTO.isM_social())
                .m_join_path(memberDTO.getM_join_path())
                .m_optional_terms(memberDTO.isM_optional_terms())
                .m_point(memberDTO.getM_point())
                .build();

        return member;
    }

    default MemberDTO entityToDTO(Member member) {

        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .m_pw(member.getM_pw())
                .m_email(member.getM_email())
                .m_del(member.isM_del())
                .m_social(member.isM_social())
                .m_join_path(member.getM_join_path())
                .m_point(member.getM_point())
                .build();

        return memberDTO;
    }
}