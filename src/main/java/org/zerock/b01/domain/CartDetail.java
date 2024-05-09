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

    private String size;      // 물품 사이즈

    private String color;     // 색상

    private int paymentSuccess; // 결제여부
    // orders로 넘어가서 결제에 성공한 장바구니 항목은 1로 수정한다. // 성은추가

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

    public void paymentSuccess(){
        // 성은추가
        // 결제성공 시 장바구니의 결제여부 값을 1로 변경한다.
        this.paymentSuccess = 1;
    }


}
