package org.zerock.b01.service;

import com.querydsl.core.types.Order;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface OrdersService {

    Long register(OrdersListDTO ordersListDTO);
    // List<OrdersDetailDTO>(안에 ItemDTO) = 선택한 상품들, AddressDTO =선택한 배송지를 매개값으로 받고 엔티티로 변환하여 insert처리
    // 자바스크립트로 username을 가져와 member entity를 찾아 insert처리
    // ${#authentication.principal.username}

    OrdersListDTO readOne(Long ono);
    // 주문 테이블 번호를 매개값으로 한 주문의 정보(배송지, 주문일자, 처리상태, 구매상품들) 가져옴

    //void remove(Long bno); 보류

    OrdersPageResponseDTO<OrdersListDTO> listWithAll(String member, OrdersPageRequestDTO ordersPageRequestDTO);
    // 페이징정보, 검색어를 가져와 주문내역을 출력

    // 결제 성공 시 해당 주문을 읽어와서 결제성공 필드를 1로 변경 후 dto로 리턴
    OrdersListDTO paymentSuccess(Long ono);


    default Orders dtoToEntity(OrdersListDTO ordersListDTO){
        Address address = Address.builder()
                .a_no(ordersListDTO.getAddressDTO().getA_no())
                .a_recipient(ordersListDTO.getAddressDTO().getA_recipient())
                .a_nickName(ordersListDTO.getAddressDTO().getA_nickName())
                .a_phone(ordersListDTO.getAddressDTO().getA_phone())
                .a_zipCode(ordersListDTO.getAddressDTO().getA_zipCode())
                .a_address(ordersListDTO.getAddressDTO().getA_address())
                .a_detail(ordersListDTO.getAddressDTO().getA_detail())
                .a_basic(ordersListDTO.getAddressDTO().getA_basic())
                .a_request(ordersListDTO.getAddressDTO().getA_request())
                .a_customRequest(ordersListDTO.getAddressDTO().getA_customRequest())
                .member(ordersListDTO.getAddressDTO().getMember())
                .a_use(ordersListDTO.getAddressDTO().getA_use())
                .build();

        Orders orders = Orders.builder()
                .ono(ordersListDTO.getOno())
                .o_ordersno(ordersListDTO.getO_ordersno())
                .member(ordersListDTO.getMember())
                .address(address) // 위에서 dtoToEntity 처리
                .o_date(ordersListDTO.getO_date())
                .o_state(ordersListDTO.getO_state())
                .totalPrice(ordersListDTO.getTotalPrice())
                .pointFirstUse(ordersListDTO.getPointFirstUse())
                .pointUse(ordersListDTO.getPointUse())
                .paymentMethod(ordersListDTO.getPaymentMethod())
                .cardCompany(ordersListDTO.getCardCompany())
                .installment(ordersListDTO.getInstallment())
                //.ordersDetailSet() 밑에서
                .paymentSuccess(ordersListDTO.getPaymentSuccess())
                .build();
        if(ordersListDTO.getOrdersDetailDTOList() != null) {
            orders.addDetail(ordersListDTO.getOrdersDetailDTOList());
        }
        return orders;
    }

    default OrdersListDTO entityToDTO(Orders orders) {

        OrdersListDTO ordersListDTO = OrdersListDTO.builder()
                .ono(orders.getOno())
                .o_ordersno(orders.getO_ordersno())
                .member(orders.getMember())
                .addressDTO(AddressDTO.builder()
                        .a_no(orders.getAddress().getA_no())
                        .a_recipient(orders.getAddress().getA_recipient())
                        .a_nickName(orders.getAddress().getA_nickName())
                        .a_phone(orders.getAddress().getA_phone())
                        .a_zipCode(orders.getAddress().getA_zipCode())
                        .a_address(orders.getAddress().getA_address())
                        .a_detail(orders.getAddress().getA_detail())
                        .a_basic(orders.getAddress().getA_basic())
                        .a_request(orders.getAddress().getA_request())
                        .a_customRequest(orders.getAddress().getA_customRequest())
                        .member(orders.getAddress().getMember())
                        .a_use(orders.getAddress().getA_use())
                        .build()) // entityToDTO 처리
                .o_date(orders.getO_date())
                .o_state(orders.getO_state())
                .totalPrice(orders.getTotalPrice())
                .pointFirstUse(orders.getPointFirstUse())
                .pointUse(orders.getPointUse())
                .paymentMethod(orders.getPaymentMethod())
                .cardCompany(orders.getCardCompany())
                .installment(orders.getInstallment())
                //.ordersDetailDTOList() 밑에서
                .paymentSuccess(orders.getPaymentSuccess())
                .build();

        List<OrdersDetailDTO> ordersDetailDTOS =
                orders.getOrdersDetailSet().stream().sorted().map(ordersDetail -> OrdersDetailDTO.builder()
                        .od_no(ordersDetail.getOd_no())
                        .orders(ordersDetail.getOrders().getOno())
                        .itemDTO(ItemDTO.builder()
                                .ino(ordersDetail.getItem().getIno())
                                .i_name(ordersDetail.getItem().getI_name())
                                .i_price(ordersDetail.getItem().getI_price())
                                .i_stock(ordersDetail.getItem().getI_stock())
                                .build())
                        .od_count(ordersDetail.getOd_count())
                        .od_size(ordersDetail.getOd_size())
                        .od_color(ordersDetail.getOd_color())
                        .od_price(ordersDetail.getOd_price())
                        .build()).collect(Collectors.toList());

        ordersListDTO.setOrdersDetailDTOList(ordersDetailDTOS);

        return ordersListDTO;
    }

}
