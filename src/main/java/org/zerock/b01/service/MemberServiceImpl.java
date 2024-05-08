package org.zerock.b01.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.dto.MemberDTO;
import org.zerock.b01.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String join(MemberJoinDTO memberJoinDTO, HttpSession session) throws midExistException {

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);

        if(exist){
            throw new midExistException();
        }

        String token = UUID.randomUUID().toString();

        session.setAttribute("joinToken", token);

        Member member = modelMapper.map(memberJoinDTO, Member.class); //엔티티 관리하는 모델 매퍼
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getM_pw()));
        member.addRole(MemberRole.USER);

        log.info("#=======================");
        log.info(member);
        log.info(token);
        log.info(member.getRoleSet());

        memberRepository.save(member);

        return token;
    }

/*    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder, MemberDTO memberDTO) throws midExistException {

        String mid = memberDTO.getMid();

        boolean exist = memberRepository.findByMid(mid);

        if(exist){
            throw new midExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class); //엔티티 관리하는 모델 매퍼
        return passwordEncoder.matches(passwordEncoder.encode(memberDTO.getM_pw()));*//*
    }*/

    public MemberDTO readMember(String mid) {

        Optional<Member> result = memberRepository.findByMid(mid);

        Member member = result.orElseThrow();

        MemberDTO memberDTO = entityToDTO(member);

        return memberDTO;
    }

    public void edit(MemberDTO memberDTO) {
/*        String mid = MemberDTO.getMid();

        Member member = modelMapper.map(memberDTO, Member.class); //엔티티 관리하는 모델 매퍼
        member.changePassword(passwordEncoder.encode(memberDTO.getM_pw()));

        log.info("=======================");
        log.info(member);

        memberRepository.save(member);*/

            Optional<Member> result = memberRepository.findByMid(memberDTO.getMid());

            Member member = result.orElseThrow();

            // 변경할 정보를 엔티티에 반영
            //member.setM_email(memberDTO.getM_email());

            // 비밀번호를 변경하려는 경우에만 변경
            if (memberDTO.getM_pw() != null && !memberDTO.getM_pw().isEmpty()) {
                member.changePassword(passwordEncoder.encode(memberDTO.getM_pw()));
            }

            // 변경된 회원 정보 저장
            memberRepository.save(member);
    }
}