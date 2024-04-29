package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // c, r, u, d 가능해야한다.

    @Query("select a from Address a order by a.a_no desc")
    List<Address> list();

    //@Query("select a from Address a order by a.a_no desc")
    List<Address> findByMember(String member);


}
