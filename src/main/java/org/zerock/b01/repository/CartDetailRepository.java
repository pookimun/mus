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

    @Query("select new org.zerock.b01.dto.CartDTO(ci.cdid, i.i_name, i.i_price, ci.count, im.imgUrl) " +
            "from CartDetail ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.cno = :cartId " +
            "and im.item.ino = ci.item.ino " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regDate desc")
    /* new com.ogu.ogoods.dto.CartDetailDto
    DTO를 직접 조회하기 위해서 new를 통해 생성자를 통해 객체 반환
    JPQL에서는 패키지와 함께 클래스명을 작성해줘야 함
    생성자 파라미터 순서는 DTO 클래스 명시 순으로 넣을 것 */
    List<CartDTO> findCartDetailDtoList(Long cartId);



}
