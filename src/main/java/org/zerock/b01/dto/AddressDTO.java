package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {

    private Long a_no; // 배송지번호

    private String a_recipient; // 수령인

    private String a_nickName; // 배송지 별칭

    private String a_phone; // 전화번호

    private String a_zipCode; // 우편번호

    private String a_address; // 주소

    private String a_detail; // 상세주소

    private int a_basic; // 기본배송지 여부

    private String a_request; // 배송 시 요청사항, '직접입력' 선택 시 작성값이 저장된다.

    private String member; // 회원

}
