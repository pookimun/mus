package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.m_id = :m_id and m.m_social = false")
    Optional<Member> getWithRoles(String m_id);


    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.m_email = :m_email and m.m_social = false")
    Optional<Member> findByM_email(String m_email);


    @Modifying
    @Transactional
    @Query("update Member m set m.m_pw =:m_pw where m.m_id = :m_id ")
    void updatePassword(@Param("m_pw") String password, @Param("m_id") String m_id);



}