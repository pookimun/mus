package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "Cart_detail")
public class CartDetail extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "cd_id")
    private Long cdid;      // pk

    @ManyToOne(fetch = FetchType.LAZY)      // cart fk
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)      // 상품 fk
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;  // 물품 카운트용

    public static CartDetail createCartDetail(Cart cart, Item item, int count) {
        CartDetail CartDetail = new CartDetail();
        CartDetail.setCart(cart);
        CartDetail.setItem(item);
        CartDetail.setCount(count);
        return CartDetail;
    }


    public void addCount(int count){
        this.count += count;
    }   // 장바구니 상품 개수 추가용

    public void updateCount(int count){
        this.count = count;
    }   // 개수 수정용


}
