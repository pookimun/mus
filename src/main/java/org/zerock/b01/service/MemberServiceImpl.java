package org.zerock.b01.service;

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


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws midExistException {

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);

        if(exist){
            throw new midExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class); //엔티티 관리하는 모델 매퍼
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getM_pw()));
        member.addRole(MemberRole.USER);

        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }

/*    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder, MemberDTO memberDTO) throws midExistException {

*//*        String mid = memberDTO.getMid();

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

/*    public void edit(MemberDTO memberDTO) {

        Optional<Member> result = memberRepository.findByMid(memberDTO.getMid());

        Member member = result.orElseThrow();

        member.changePassword(memberDTO.getM_pw());

        memberRepository.save(member);
    }*/

    public void edit(MemberDTO memberDTO) {

    }
}