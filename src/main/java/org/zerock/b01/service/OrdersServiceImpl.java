package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.*;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService{

    private final ModelMapper modelMapper;

    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;


    @Transactional
    @Override
    public Long register(OrdersListDTO ordersListDTO) {
        log.info("service 영역에서 register 실행 ... ");
        Orders orders = dtoToEntity(ordersListDTO);
        // 부모 엔티티인 Orders 먼저 save 후에
        log.info(orders);
        Orders savedOrders = ordersRepository.save(orders);
        for(OrdersDetail ordersDetail : savedOrders.getOrdersDetailSet()){
            ordersDetail.changeOrders(savedOrders);
        }
        // 자식 엔티티엔 OrdersDetail save
        ordersDetailRepository.saveAll(savedOrders.getOrdersDetailSet());
        // orders의 orderDetailSet을 반복하며 ordersDetailRepository의 save 메서드를 실행
        log.info(savedOrders);
        return savedOrders.getOno();
    }

    @Override
    public OrdersListDTO readOne(Long ono) {
        Optional<Orders> result = ordersRepository.findByIdWithOrdersDetails(ono);
        Orders orders = result.orElseThrow();
        OrdersListDTO ordersListDTO = entityToDTO(orders);
        return ordersListDTO;
    }

    @Override
    public OrdersListDTO paymentSuccess(Long ono) {
        Optional<Orders> result = ordersRepository.findByIdWithOrdersDetails(ono);
        Orders orders = result.orElseThrow();
        orders.paymentSuccess(); // 결제성공 저장!
        ordersRepository.save(orders); // update
        OrdersListDTO ordersListDTO = entityToDTO(orders);
        // 장바구니의 결제여부 값을 변경!
//        Optional<Cart> cart = cartRepository.findById() OrdersDetail에 장바구니 번호 필드를 추가해야할것같음!
        // 그리고 위에서 받아온 장바구니 번호 Cart의 paymentSuccess 필드 값을 1로 변경해준다 !!
        return ordersListDTO;
    }

    @Override
    public OrdersPageResponseDTO<OrdersListDTO> listWithAll(String member, OrdersPageRequestDTO ordersPageRequestDTO) {
        log.info("listWithAll 실행 ... ");
        String keyword = ordersPageRequestDTO.getKeyword();
        Pageable pageable = ordersPageRequestDTO.getPageable("ono");

        Page<OrdersListDTO> result = ordersRepository.searchWithAll(member, keyword, pageable);
        log.info("searchWithAll result : " + result);

        return OrdersPageResponseDTO.<OrdersListDTO>pageResponse()
                .ordersPageRequestDTO(ordersPageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }
}
