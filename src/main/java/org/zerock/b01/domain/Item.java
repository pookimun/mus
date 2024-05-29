package org.zerock.b01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.zerock.b01.constant.ItemSellStatus;


import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "item")
@Getter @Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity{

    @Id
//    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino; //상품  (pk)

    @Column(nullable = false) // not null
    @NotEmpty
    private String i_name; //상품명

    @Column(length = 100, nullable = false) // not null
    private int i_price; //상품 가격

    private String i_color;

    private String i_size;

    // not null
    private String i_title_img; //대표이미지

    // not null
    private String i_info_img; //설명이미지

    // not null
    private int i_stock; //재고보유여부


    @OneToMany (mappedBy = "item",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true) //itemimage 의 item 변수
    @Builder.Default
    @BatchSize(size=20)
    private Set<ItemImage> itemImageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        ItemImage itemImage = ItemImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .item(this)
                .ord(itemImageSet.size())
                .build();
        itemImageSet.add(itemImage);
    }
    public void clearImages() {

        itemImageSet.forEach(ItemImage -> ItemImage.changeItem(null));

        this.itemImageSet.clear();

    }



    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태


    public void change(String i_name, int i_price, String i_color, String i_size) {
        this.i_name = i_name;
        this.i_price = i_price;
        this.i_color = i_color;
        this.i_size = i_size;


    }




}
