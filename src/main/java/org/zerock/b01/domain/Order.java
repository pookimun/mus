package org.zerock.b01.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "address"}) // 나중에 fk 추가 시 exclude 하기
@EntityListeners(value = { AuditingEntityListener.class })
public class Order {

    @Id
    @Column(length = 50)
    private String o_no;
    // 주문번호(YYMMdd)+랜덤문자열 형식의 12글자 길이

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Member member; // 회원

    @OneToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Address address; // 주문 시 선택한 배송지

    @CreatedDate
    @Column(name = "o_date", updatable = false, nullable = false)  // not null
    //@JsonFormat(pattern = "yyyy-MM-dd") // LocalDateTime 타입 포멧 지정(작동안됨)
    private LocalDateTime o_date; // 주문일자

    @Column(length = 50, nullable = false) // not null
    private String o_state; // 주문 처리 상태

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 실행
    public void insertO_orderno() {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomNumber = String.format("%06d", new Random().nextInt(1000000)); // 여섯자리 랜덤숫자 생성
        // 오늘 날짜와 랜덤 숫자를 조합하여 o_orderno 설정
        this.o_no = todayDate + randomNumber; // 12자리
    }


}
