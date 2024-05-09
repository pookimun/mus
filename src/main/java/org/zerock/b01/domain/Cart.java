package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;      // 멤버랑 엮음 fk // user

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜


    @PrePersist
    public void createDate(){
        this.createDate = LocalDate.now();
    }


    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }





}
