package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "address")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString // 나중에 fk 추가 시 exclude 하기
public class Address {
    // index 생성해둠!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long a_no; // 배송지번호

    @Column(length = 100, nullable = false) // not null
    private String a_recipient; // 수령인

    @Column(length = 100)
    private String a_nickName; // 배송지 별칭 = 별칭 없을 시 '수령인님 배송지'

    @Column(length = 30, nullable = false) // not null
    private String a_phone; // 전화번호

    @Column(length = 10, nullable = false) // not null
    private String a_zipCode; // 우편번호

    @Column(length = 300, nullable = false) // not null
    private String a_address; // 주소

    @Column(length = 200, nullable = false) // not null
    private String a_detail; // 상세주소

    @Column(nullable = false) // not null
    private int a_basic; // 기본배송지 여부

    @Column(length = 500, nullable = false) // not null
    private String a_request; // 배송 시 요청사항, '직접입력' 선택 시 작성값이 저장된다.

    //private Long m_no; 회원번호 : fk 예정


    // 수령인, 배송지 별칭, 휴대전화, 배송지주소(우편번호, 주소, 상세주소), 기본배송지 여부, 요청사항 변경가능 !!
    public void change(String a_recipient, String a_nickName, String a_phone, String a_zipCode, String a_address, String a_detail, int a_basic){
        // 수령인, 배송지별칭, 휴대전화, 배송지주소(우편번호, 주소, 상세주소), 기본배송지 여부 변경
        this.a_recipient = a_recipient;
        this.a_nickName = a_nickName;
        this.a_phone = a_phone;
        this.a_zipCode = a_zipCode;
        this.a_address = a_address;
        this.a_detail = a_detail;
        this.a_basic = a_basic;
    }

    public void changeRequest(String a_request){
        // 요청사항 변경, 주문서에서 이거 하나만 변경할 수 있도록 하기 위해 따로 뺌
        this.a_request = a_request;
    }




}
