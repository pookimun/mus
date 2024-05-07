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

    private int i_stock; //재고보유여부

    private String itemDetail;  // 상품 상세정보

    private ItemSellStatus itemSellStatus;





}
