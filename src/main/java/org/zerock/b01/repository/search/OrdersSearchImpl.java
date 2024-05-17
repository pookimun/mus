package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.*;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersSearchImpl extends QuerydslRepositorySupport implements OrdersSearch {
    // 주문내역 조회 시 기간, 상품명&브랜드명으로 검색 기능
    // 페이징 기능
    // order_detail도 같이 가져온다.

    public OrdersSearchImpl() {
        super(Orders.class);
    } // QuerydslRepositorySupport 생성자 필요

    @Override
    public Page<OrdersListDTO> searchWithAll(String member, String keyword, Pageable pageable) {
        // 상품명, 브랜드명으로 검색 기능 구현 완료
        // 페이징 구현 완료
        // order_detail도 같이 가져오기 구현완료 ㅜㅜ

        // Q도메인 객체 가져옴
        QOrders orders = QOrders.orders;
        QOrdersDetail ordersDetail = QOrdersDetail.ordersDetail;
        QItem item = QItem.item;

        // from order left join orderDetail left join item
        JPQLQuery<Orders> ordersJPQLQuery = from(orders);
        ordersJPQLQuery.leftJoin(ordersDetail).on(ordersDetail.orders.eq(orders)); //left join
        ordersJPQLQuery.leftJoin(item).on(item.eq(ordersDetail.item)); //left join

        if (keyword != null) { // 상품명으로 검색
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // ()
            booleanBuilder.or(ordersDetail.item.i_name.contains(keyword));
            ordersJPQLQuery.where(booleanBuilder);
        }
        ordersJPQLQuery.where(orders.member.eq(member));
        ordersJPQLQuery.where(orders.paymentSuccess.eq(1));

        ordersJPQLQuery.groupBy(orders);

        getQuerydsl().applyPagination(pageable, ordersJPQLQuery); //paging

        JPQLQuery<Orders> jpqlQuery = ordersJPQLQuery.select(orders); // select orders

        List<Orders> ordersList = jpqlQuery.fetch(); // 쿼리 실행
        System.out.println(ordersList);

        List<OrdersListDTO> dtoList = ordersList.stream().map(orders1 -> {
            // 조인 처리된 테이블 한 행씩 반복하며(Order entity)

            // List<OrderListDTO> 타입의 dto 생성
            OrdersListDTO dto = OrdersListDTO.builder()
                    .ono(orders1.getOno())
                    .o_ordersno(orders1.getO_ordersno())
                    .member(orders1.getMember())
                    //.addressDTO() 밑에서
                    .o_date(orders1.getO_date())
                    .o_state(orders1.getO_state())
                    //.orderDetailDTOList() 밑에서
                    .totalPrice(orders1.getTotalPrice())
                    .pointFirstUse(orders1.getPointFirstUse())
                    .pointUse(orders1.getPointUse())
                    .paymentMethod(orders1.getPaymentMethod())
                    .cardCompany(orders1.getCardCompany())
                    .installment(orders1.getInstallment())
                    .paymentSuccess(orders1.getPaymentSuccess())
                    .build();
            // order1.address를 AddressDTO 처리
            AddressDTO addressDTO = AddressDTO.builder()
                    .a_no(orders1.getAddress().getA_no())
                    .a_recipient(orders1.getAddress().getA_recipient())
                    .a_nickName(orders1.getAddress().getA_nickName())
                    .a_phone(orders1.getAddress().getA_phone())
                    .a_zipCode(orders1.getAddress().getA_zipCode())
                    .a_address(orders1.getAddress().getA_address())
                    .a_detail(orders1.getAddress().getA_detail())
                    .a_basic(orders1.getAddress().getA_basic())
                    .a_request(orders1.getAddress().getA_request())
                    .a_customRequest(orders1.getAddress().getA_customRequest())
                    .member(orders1.getAddress().getMember())
                    .a_use(orders1.getAddress().getA_use())
                    .build();
            dto.setAddressDTO(addressDTO);

            // order1.orderDetailSet을 List<OrderDetailDTO> 처리
            List<OrdersDetailDTO> detailDTOS = orders1.getOrdersDetailSet().stream().sorted()
                    .map(detail -> OrdersDetailDTO.builder()
                            .od_no(detail.getOd_no())
                            .orders(detail.getOrders().getOno())
                            .itemListAllDTO(ItemListAllDTO.builder()
                                    .ino(detail.getItem().getIno())
                                    .i_name(detail.getItem().getI_name())
                                    .i_price(detail.getItem().getI_price())
                                    .i_stock(detail.getItem().getI_stock())
                                    .itemImages(detail.getItem().getItemImageSet().stream()
                                            .map(itemImage -> ItemImageDTO.builder()
                                                    .uuid(itemImage.getUuid())
                                                    .fileName(itemImage.getFileName())
                                                    .ord(itemImage.getOrd())
                                                    .build()) // ItemImage의 fileName을 가져와서
                                            .collect(Collectors.toList())) // List로 저장
                                    .build())
                            .od_count(detail.getOd_count())
                            .od_size(detail.getOd_size())
                            .od_color(detail.getOd_color())
                            .od_price(detail.getOd_price())
                            .build())
                    .collect(Collectors.toList());
            dto.setOrdersDetailDTOList(detailDTOS);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = ordersJPQLQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, totalCount);
        // Page<OrdersListDTO>
    }

}

















