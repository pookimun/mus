package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

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


    //private int c_count;        // 장바구니에 담은 수

    private String c_size;      // 물품 사이즈

    private String c_color;     // 색상

    private int paymentSuccess; // 결제여부
    // orders로 넘어가서 결제에 성공한 장바구니 항목은 1로 수정한다. // 성은추가

    public static Cart createCart(Member member) { // cart 객체 생성용
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

    public void paymentSuccess(){
        // 성은추가
        // 결제성공 시 장바구니의 결제여부 값을 1로 변경한다.
        this.paymentSuccess = 1;
    }






}
