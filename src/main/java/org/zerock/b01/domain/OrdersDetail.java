package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orders", "item"}) // 나중에 fk 추가 시 exclude 하기
public class OrdersDetail implements Comparable<OrdersDetail> {
    // Comparable<OrdersDetail> @OneToMany 처리에서 순번에 맞게 정렬하기 위함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long od_no; // 주문상세번호

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Orders orders; // 주문

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
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

    @Column(nullable = false) // not null
    private Long cdid; // 주문을 원하는 장바구니 번호들

    @Override
    public int compareTo(OrdersDetail other) {
        // OrderDetail 간의 순서를 정한다. od_no를 기준으로 순서를 정했다.
        return Long.compare(this.od_no, other.od_no);
    }

    public void changeOrders(Orders orders){
        // Orders 객체 삭제시 참조 변경용
        this.orders = orders;
    }





}
