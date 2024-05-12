package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemListAllDTO {

    private Long ino;

    private String i_name;

    private int i_price; // 성은추가

    private String i_color; //i_price 에서 변경

    private String i_size; // 성은추가

    private int i_stock; //재고보유여부

    private List<ItemImageDTO> itemImages;

}
