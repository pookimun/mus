package org.zerock.b01.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartAllDTO {
    // 성은추가 !!
    // Cart와 CartDetail의 정보를 DTO로 불러오기 위함

    private Long cno;
    private String member;
    //private int c_count;
    private List<String> sizes;
    private List<String> colors;
    private List<Integer> paymentSuccesss;
    private List<Long> cdids;
    // 하나의 cart에 여러개의 cartDetail
    private List<ItemDTO> itemDTOS;
    private List<Integer> counts;


}
