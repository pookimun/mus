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
public class OrdersDTO {
    // 주문서에서 결제하기 버튼을 누르면 아래 정보를 가지고 이동한다.

    // 배송지 번호
    private Long ano;

    // 주문 처리 상태
    private String o_state;

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

    // 상품 번호들
    private Long[] inos;

    // 상품들의 구매수량
    private int[] counts;

    // 상품들의 사이즈
    private String[] sizes;

    // 상품들의 색상
    private String[] colors;

    // 구매를 원하는 장바구니 디테일 번호들
    private Long[] cdids;


}

