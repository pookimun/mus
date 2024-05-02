package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Cart;
import org.zerock.b01.domain.CartDetail;
import org.zerock.b01.domain.Item;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    CartDetail findByCart_CnoAndItem_Ino(Long cartId, Long itemId);



}
