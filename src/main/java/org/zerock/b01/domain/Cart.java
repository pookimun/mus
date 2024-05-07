package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cart")
public class Cart extends BaseEntity{

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;           // 장바구니 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;      // 멤버랑 엮음 fk


    private int c_count;        // 장바구니에 담은 수

    private String c_size;      // 물품 사이즈

    private String c_color;     // 색상

    public static Cart createCart(Member member) {      // cart 객체 생성용
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }





}
