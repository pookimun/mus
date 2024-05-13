package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDetailDTO {
    // OrderListDTO에서 orderDetailDTOList 필드에 처리 될 dto

    // 주문 상세 번호
    private Long od_no;

    // 주문 테이블 번호
    private Long orders;

    // 선택한 상품
    private ItemListAllDTO itemListAllDTO;

    // 선택한 상품의 구매 수량
    private int od_count;

    // 선택한 상품의 사이즈
    private String od_size;

    // 선택한 상품의 색상
    private String od_color;

    // 주문 당시의 상품 가격
    // 주문내역을 조회할 때 현재가격이 아니라 주문 당시의 가격을 표시해야 하기 때문에 가격 필드를 따로 생성생성
    private int od_price;

}
