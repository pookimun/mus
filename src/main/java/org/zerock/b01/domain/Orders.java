package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.zerock.b01.dto.OrdersDetailDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "address"}) // 나중에 fk 추가 시 exclude 하기
@EntityListeners(value = { AuditingEntityListener.class })
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono; // 주문테이블 번호

    @Column(length = 50, nullable = false)
    private String o_ordersno;
    // 주문번호(YYMMdd)+랜덤문자열 형식의 12글자 길이

    //@ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    //@JoinColumn(nullable = false) // not null
    @Column(length = 50, nullable = false) // not null
    private String member; // 회원

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 사용
    @JoinColumn(nullable = false) // not null
    private Address address; // 주문 시 선택한 배송지

    @CreatedDate
    @Column(name = "o_date", updatable = false, nullable = false)  // not null
    //@JsonFormat(pattern = "yyyy-MM-dd") // LocalDateTime 타입 포멧 지정(작동안됨)
    private LocalDateTime o_date; // 주문일자

    @Column(length = 50, nullable = false) // not null
    private String o_state; // 주문 처리 상태

    @OneToMany(mappedBy = "orders",
            cascade = {CascadeType.ALL}, // 상위엔티티(orders)의 변경이 하위엔티티(order_detail)에 적용
            fetch = FetchType.LAZY, // 지연로딩
            orphanRemoval = true) // 상위엔티티(orders)에서 제거된 하위엔티티(order_detail)를 자동으로 삭제할지 여부
    // @Builder.Default : 해당 필드가 null이 아니고 비어있는 HashSet으로 초기화되도록 보장한다.
    @Builder.Default
    @BatchSize(size = 20)
    private Set<OrdersDetail> ordersDetailSet = new HashSet<>();

    // 총 결제금액
    @Column(nullable = false)
    private int totalPrice;

    // 적립금 선할인 금액
    private int pointFirstUse;

    // 적립금 사용금액
    private int pointUse;

    // 결제수단
    @Column(nullable = false)
    private String paymentMethod;

    // 일반결제 시 카드사
    private String cardCompany;

    // 일반결제 시 할부정보
    private int installment;

    // 결제성공 여부
    private int paymentSuccess;

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 실행
    public void insertO_ordersno() {
        this.o_ordersno = o_ordersnoCreate();
    }

    public void addDetail(List<OrdersDetailDTO> ordersDetailDTOS){
        // ordersDetailDTO의 ItemDTO를 Item entity로 변환
        ordersDetailDTOS.forEach(ordersDetailDTO -> {
        Item item = Item.builder()
                .ino(ordersDetailDTO.getItemDTO().getIno())
                .i_name(ordersDetailDTO.getItemDTO().getI_name())
                .i_price(ordersDetailDTO.getItemDTO().getI_price())
                .i_stock(ordersDetailDTO.getItemDTO().getI_stock())
                .build();

        OrdersDetail ordersDetail = OrdersDetail.builder()
                .od_no(ordersDetailDTO.getOd_no())
                .orders(this)
                .item(item)
                .od_count(ordersDetailDTO.getOd_count())
                .od_size(ordersDetailDTO.getOd_size())
                .od_color(ordersDetailDTO.getOd_color())
                .od_price(ordersDetailDTO.getOd_price())
                .build();
        ordersDetailSet.add(ordersDetail); // Set<OrdersDetail>에 추가
        });
    }

    public void paymentSuccess(){
        this.paymentSuccess = 1;
    } // 결제성공 메서드
    public void newO_ordersno() {
        this.o_ordersno = o_ordersnoCreate();
    }

    public String o_ordersnoCreate(){ // 주문번호 생성 메서드
        System.out.println("newO_ordersno() 실행 ~!~!~!~!~!~!~!~!~!~!");
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomNumber = String.format("%06d", new Random().nextInt(1000000)); // 여섯자리 랜덤숫자 생성
        // 오늘 날짜와 랜덤 숫자를 조합하여 o_orderno 설정
        System.out.println(randomNumber);
        return todayDate + randomNumber; // 12자리
    }


}
