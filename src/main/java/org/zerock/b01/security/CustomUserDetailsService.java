package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 사용자 이름을 기반으로 사용자 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);

        // 사용자 이름을 기반으로 회원 정보와 역할을 함께 가져옴
        Optional<Member> result = memberRepository.getWithRoles(username);

        // 회원 정보가 없으면 예외를 던짐
        if(result.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        // 회원 정보를 가져옴
        Member member = result.get();

        // 회원 정보를 기반으로 MemberSecurityDTO 객체 생성
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(), // 사용자 아이디
                member.getM_pw(), // 사용자 비밀번호
                member.getM_email(), // 사용자 이메일
                member.isM_del(), // 사용자 삭제 여부
                false, // 임시 설정 값 (무엇을 나타내는지 확인 필요)
                // 회원의 역할(Role)을 스트림으로 변환하여 SimpleGrantedAuthority로 매핑한 뒤 리스트로 수집
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority
                        ("ROLE_" + memberRole.name())).collect(Collectors.toList())
        );

        // 생성된 MemberSecurityDTO 객체 로그로 출력
        log.info("memberSecurityDTO---------");
        log.info(memberSecurityDTO);

        // MemberSecurityDTO 반환
        return memberSecurityDTO;
    }
}
