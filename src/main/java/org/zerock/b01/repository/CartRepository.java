package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMember_Mid(String mid);

}
