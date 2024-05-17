package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Cart;
import org.zerock.b01.domain.CartDetail;
import org.zerock.b01.domain.Item;
import org.zerock.b01.dto.CartDTO;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    CartDetail findByCart_CnoAndItem_Ino(Long cno, Long itemId);

    @Query("select new org.zerock.b01.dto.CartDTO(cd.cdid, i.i_name, i.i_price, cd.count, im.fileName, im.uuid) " +
            "from CartDetail cd " +
            "join cd.item i " +
            "join ItemImage im on im.item.ino = i.ino " +
            "where cd.cart.cno = :cartId " +
            "group by i.ino " +
            //"HAVING im.ord = 0 " +
            "order by cd.regDate desc")
    List<CartDTO> findCartDetailDtoList(Long cartId);

    @Query("select cd from CartDetail cd where cd.cart = :cart and cd.paymentSuccess = 0")
    List<CartDetail> findByCartWherePaymentSuccess(Cart cart);
    // 성은추가!! 한 장바구니의 세부 항목들 중 결제되지 않은 항목만을 리턴함



}
