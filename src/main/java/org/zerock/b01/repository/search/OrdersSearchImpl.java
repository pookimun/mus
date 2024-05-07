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

    public OrdersSearchImpl(){
        super(Orders.class);
    } // QuerydslRepositorySupport 생성자 필요

    @Override
    public Page<OrdersListDTO> searchWithAll(String member, String keyword, Pageable pageable) {
        // 상품명, 브랜드명으로 검색 기능 구현 완료
        // 페이징 구현 완료
        // order_detail도 같이 가져오기 구현완료
        // 멤버 연결 필요 !

        // Q도메인 객체 가져옴
        QOrders orders = QOrders.orders;
        QOrdersDetail ordersDetail = QOrdersDetail.ordersDetail;
        QItem item = QItem.item;

        // from order left join orderDetail left join item
        JPQLQuery<Orders> ordersJPQLQuery = from(orders);
        ordersJPQLQuery.leftJoin(ordersDetail).on(ordersDetail.orders.eq(orders)); //left join
        ordersJPQLQuery.leftJoin(item).on(item.eq(ordersDetail.item)); //left join

        if( keyword != null ){ // 상품명으로 검색
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // ()
            booleanBuilder.or(ordersDetail.item.i_name.contains(keyword));
            ordersJPQLQuery.where(booleanBuilder);
        }
        ordersJPQLQuery.where(orders.member.eq(member));
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
                    .member(orders1.getAddress().getMember())
                    .build();
            dto.setAddressDTO(addressDTO);

            // order1.orderDetailSet을 List<OrderDetailDTO> 처리
            List<OrdersDetailDTO> detailDTOS = orders1.getOrdersDetailSet().stream().sorted()
                    .map(detail -> OrdersDetailDTO.builder()
                            .od_no(detail.getOd_no())
                            .itemDTO(ItemDTO.builder()
                                    .ino(detail.getItem().getIno())
                                    .i_name(detail.getItem().getI_name())
                                    .i_price(detail.getItem().getI_price())
                                    .i_title_img(detail.getItem().getI_title_img())
                                    .i_info_img(detail.getItem().getI_info_img())
                                    .i_color(detail.getItem().getI_color())
                                    .i_size(detail.getItem().getI_size())
                                    .i_stock(detail.getItem().getI_stock())
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
        // OrdersListDTO(o_no=100,
        // o_ordersno=240428377304,
        // addressDTO=AddressDTO(a_no=28, a_recipient=member28, a_nickName=님 배송지, a_phone=01012345678, a_zipCode=12345, a_address=경기도 수원시 팔달로, a_detail=28호, a_basic=1, a_request=부재 시 문 앞에 놓아주세요., memberJoinDTO=null),
        // o_date=2024-04-28T16:57:20.490275,
        // o_state=주문접수,
        // ordersDetailDTOList=
        // [OrdersDetailDTO(od_no=100, itemDTO=ItemDTO(i_no=31, i_name=상품 ... 31, i_price=10031, i_title_img=대표이미지.jpg, i_info_img=설명이미지.jpg, i_color=white, i_size=95, i_stock=1), od_count=1, od_size=95, od_color=white, od_price=10031),
        // OrdersDetailDTO(od_no=200, itemDTO=ItemDTO(i_no=13, i_name=상품 ... 13, i_price=10013, i_title_img=대표이미지.jpg, i_info_img=설명이미지.jpg, i_color=white, i_size=95, i_stock=1), od_count=1, od_size=95, od_color=white, od_price=10013)])
        long totalCount = ordersJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

}

















