package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.AddressRepository;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.repository.ItemRepository;
import org.zerock.b01.repository.OrdersRepository;

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
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;


    @Override
    public Long register(OrdersListDTO ordersListDTO) {
        Orders orders = dtoToEntity(ordersListDTO);
        Long ono = ordersRepository.save(orders).getOno();
        return ono;
    }

    @Override
    public OrdersListDTO readOne(Long ono) {
        Optional<Orders> result = ordersRepository.findByIdWithOrderDetails(ono);
        Orders orders = result.orElseThrow();
        OrdersListDTO ordersListDTO = entityToDTO(orders);
        return ordersListDTO;
    }

    @Override
    public OrdersPageResponseDTO<OrdersListDTO> listWithAll(OrdersPageRequestDTO ordersPageRequestDTO) {
        String keyword = ordersPageRequestDTO.getKeyword();
        Pageable pageable = ordersPageRequestDTO.getPageable("ono");

        Page<OrdersListDTO> result = ordersRepository.searchWithAll(keyword, pageable);

        return OrdersPageResponseDTO.<OrdersListDTO>pageResponse()
                .ordersPageRequestDTO(ordersPageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }
}
