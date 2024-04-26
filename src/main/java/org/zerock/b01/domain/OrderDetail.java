package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "order_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"order", "item"}) // 나중에 fk 추가 시 exclude 하기
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long od_no; // 주문상세번호

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Order order; // 주문

    @OneToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Item item; // 선택한 상품

    @Column(nullable = false) // not null
    private int od_count; // 선택한 상품의 구매할 수량

    @Column(length = 50, nullable = false) // not null
    private String od_size; // 선택한 상품의 사이즈

    @Column(length = 50, nullable = false) // not null
    private String od_color; // 선택한 상품의 색상

    @Column(nullable = false) // not null
    private int od_price; // 주문 당시의 상품 가격
    // 주문내역을 조회할 때 현재가격이 아니라 주문 당시의 가격을 표시해야 하기 때문에 가격 필드를 따로 생성




}
