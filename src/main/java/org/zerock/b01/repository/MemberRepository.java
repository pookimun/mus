package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> { //JPARepository를 상속 받는 MemberRepository

    @EntityGraph(attributePaths = "roleSet") //Member 엔티티를 조회할 때 RoleSet 필드 로딩 (EntityGraph: 엔티티 조회 시 즉시 로딩 or 지연 로딩)
    @Query("select m from Member m where m.mid = :mid and m.m_social = false")
    //회원 권한 검색
    Optional<Member> getWithRoles(String mid); //mid로 Member 엔티티를 검색하는 동시에 회원 권한(roleSet) 정보도 함께 검색

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.m_email = :m_email and m.m_social = false")
    //회원 이메일 검색
    Optional<Member> findByM_email(String m_email);

    @EntityGraph(attributePaths = "roleSet")
    // @EntityGraph에는 attributePaths라는 속성을 이용해서 같이 로딩해야 하는 속성 명시
    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> findByMid(String mid);

    /*@Modifying //DB 수정
    @Transactional //성공 => 커밋, 예외 => 롤백
    @Query("update Member m set m.m_pw = :m_pw where m.mid = :mid ")
    //비밀번호 (등록)변경
    void updatePassword(@Param("m_pw") String newPassword, @Param("mid") String mid);*/
}