package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString // 나중에 fk 추가 시 exclude 하기
@EntityListeners(value = { AuditingEntityListener.class })
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long o_no; // pk용 order 테이블 번호

    @Column(length = 100, nullable = false) // not null
    private String o_orderno; // 주문번호(밀리초 단위로 생성 예정, 한 주문단위를 묶기위함)

    // private Long p_no; 상품번호 fk 예정 ... not null

    // private Long m_no; 회원번호 fk 예정 ... not null

    @CreatedDate
    @Column(name = "o_date", updatable = false) // not null
    private LocalDateTime o_date; // 주문일자

    @Column(nullable = false) // not null
    private int o_count; // 선택한 상품의 주문 수량

    @Column(length = 50, nullable = false) // not null
    private String o_size; // 선택한 상품의 사이즈

    @Column(length = 50, nullable = false)
    private String o_color; // 선택한 상품의 색상

    @Column(length = 100, nullable = false) // not null
    private String o_state; // 주문 처리 상태


}
