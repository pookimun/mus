package org.zerock.b01.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotEmpty
    private String a_recipient; // 수령인

    @NotEmpty
    private String a_nickName; // 배송지 별칭

    @NotEmpty
    @Pattern(regexp = "^\\d{10,11}$")
    // 전화번호 입력 검증 패턴, 패턴이 맞지 않으면 오류 발생 ex)01012345678(정수 10자리 또는 정수 11자리)
    private String a_phone; // 전화번호

    @NotEmpty
    private String a_zipCode; // 우편번호

    @NotEmpty
    private String a_address; // 주소

    @NotEmpty
    private String a_detail; // 상세주소

    private int a_basic; // 기본배송지 여부

    @NotEmpty
    private String a_request; // 배송 시 요청사항, '직접입력' 선택 시 작성값이 저장된다.

    private String a_customRequest; // 직접입력 선택 시 작성란

    @NotEmpty
    private String member; // 회원

    private int a_use; // 배송지 사용 여부

    //private int basicYesOrNo;
    // register.html에서 기본배송지로 배송지를 등록하려고 할 때 여기로 1을 전송(엔티티까지 갈 필요는 없는 필드)

}
