package org.zerock.b01.dto;

import lombok.*;

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
    private String c_size;
    private String c_color;
    private int paymentSuccess;
    private Long cdid;
    private ItemDTO itemDTO;
    private int count;


}
