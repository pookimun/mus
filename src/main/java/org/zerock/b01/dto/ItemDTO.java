package org.zerock.b01.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.b01.constant.ItemSellStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long ino; //상품  (pk)

    private String i_name; //상품명

    private int i_price; //상품 가격

    private String i_title_img; //대표이미지

    private String i_info_img; //설명이미지

    private String i_color; //색상

    private String i_size; //사이즈

    private int i_stock; //재고보유여부

    private List<String> fileNames; //첨부 파일 이름들

    private ItemSellStatus itemSellStatus;





}
