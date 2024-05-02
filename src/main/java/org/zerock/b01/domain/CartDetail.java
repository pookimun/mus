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
    private Long cdid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    public static CartDetail createCartDetail(Cart cart, Item item, int count) {
        CartDetail CartDetail = new CartDetail();
        CartDetail.setCart(cart);
        CartDetail.setItem(item);
        CartDetail.setCount(count);
        return CartDetail;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }


}
