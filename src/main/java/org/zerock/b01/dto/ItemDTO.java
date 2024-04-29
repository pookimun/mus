package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    // 상품번호
    private Long i_no;

    // 상품명
    private String i_name;

    // 가격
    private int i_price;

    // 대표이미지
    private String i_title_img;

    // 설명이미지
    private String i_info_img;

    // 색상
    private String i_color;

    // 사이즈
    private String i_size;

    // 재고 보유 여부
    private int i_stock;

}
