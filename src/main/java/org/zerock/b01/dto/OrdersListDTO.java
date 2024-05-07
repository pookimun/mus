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
public class OrdersListDTO {
    // 주문내역을 출력할 때의 dto

    // 주문 테이블 번호 pk
    private Long ono;

    // 주문번호
    private String o_ordersno;

    // 회원 dto
    private String member; // fk 나중에 ...

    // 배송지 dto
    private AddressDTO addressDTO; // fk

    // 주문일자
    private LocalDateTime o_date;

    // 주문 처리 상태
    private String o_state;

    // 한 주문 건 당 구매했던 상품들의 정보(상세주문내역 번호)
    private List<OrdersDetailDTO> ordersDetailDTOList;
    //private List<Long> o_ordersDetailNo;

    // 총 결제금액
    private int totalPrice;

    // 적립금 선할인 금액
    private int pointFirstUse;

    // 적립금 사용금액
    private int pointUse;

    // 결제수단
    // toss, kakaopay, payco, pg
    private String paymentMethod;

    // 일반결제 시 카드사
    private String cardCompany;

    // 일반결제 시 할부정보
    private int installment;

    // 결제성공 여부
    private int paymentSuccess;




}
