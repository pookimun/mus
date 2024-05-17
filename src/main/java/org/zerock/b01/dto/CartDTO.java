package org.zerock.b01.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
public class CartDTO {

    private Long cartItemId; // 장바구니 상품 아이디..
    private String itemNm; // 상품명
    private int price; // 상품 금액
    private int count; // 수량
    private String fileName;
    private String uuid;

    public CartDTO(Long cartItemId, String itemNm, int price, int count, String fileName, String uuid) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.fileName = fileName;
        this.uuid = uuid;
    }

}