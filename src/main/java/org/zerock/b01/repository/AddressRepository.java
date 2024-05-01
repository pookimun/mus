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
    List<Address> list(); // 안쓸듯

    @Query("select a from Address a where a.member = :member and a.a_use = 1 order by a.a_basic desc, a.a_no asc")
    List<Address> memberAddressList(String member);
    // member를 받아서 주소 리스트를 가져오는 쿼리(기본배송지가 맨 위에 있게하고, 사용중인 배송지만 띄운다.)

    @Query("select count(a) from Address a where a.member = :member and a.a_use = 1")
    int memberAddressListCount(String member);
    // member를 받아 사용중인 배송지 리스트의 개수를 반환한다.

    @Query("select a from Address a where a.member = :member and a.a_basic = 1")
    List<Address> memberAddressBasic(String member);
    // member를 받아 기본배송지로 저장된 Address를 가져온다.

}
