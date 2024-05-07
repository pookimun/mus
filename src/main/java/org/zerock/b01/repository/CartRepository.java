package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Cart;
import org.zerock.b01.dto.CartAllDTO;

public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findByMember_Mid(String mid);




}
