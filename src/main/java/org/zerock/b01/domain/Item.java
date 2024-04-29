package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(value = {AuditingEntityListener.class })
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino; //상품  (pk)

    @Column(nullable = false) // not null
    private String i_name; //상품명

    @Column(length = 100, nullable = false) // not null
    private int i_price; //상품 가격

    @Column(nullable = false) // not null
    private String i_title_img; //대표이미지

    @Column(nullable = false) // not null
    private String i_info_img; //설명이미지

    @Column(length = 50, nullable = false)
    private String i_color; //색상

    @Column(length = 50, nullable = false) // not null
    private String i_size; //사이즈

    @Column(nullable = false) // not null
    private int i_stock; //재고보유여부

    public void  change(String i_name,int i_price,String i_color,String i_size) {
        this.i_name = i_name;
        this.i_price = i_price;
        this.i_color = i_color;
        this.i_size = i_size;
    }
}
