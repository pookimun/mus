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
public class Cart extends BaseEntity{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;           // 장바구니 번호

    @Column(nullable = false)
    private int c_count;        // 장바구니에 담은 수

    private String c_size;      // 물품 사이즈

    private String c_color;     // 색상

    private Date c_date;        // 담은 날짜

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;      // 멤버랑 엮음 fk


    public static Cart createCart(Member member) {      // cart 객체 생성용
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }





}
