package org.zerock.b01.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.dto.AddressDTO;
import org.zerock.b01.dto.ItemDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.repository.AddressRepository;
import org.zerock.b01.repository.ItemRepository;
import org.zerock.b01.repository.OrdersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService{
    private final ModelMapper modelMapper;

    private final AddressRepository addressRepository;
    private final OrdersRepository ordersRepository;

    @Override
    public Long register(AddressDTO addressDTO) {
        Address address = dtoToEntity(addressDTO);
        Long a_no = addressRepository.save(address).getA_no();
        return a_no;
    }

    @Override
    public AddressDTO readOne(Long a_no) {
        Optional<Address> result = addressRepository.findById(a_no);
        Address address = result.orElseThrow();
        AddressDTO addressDTO = entityToDTO(address);
        return addressDTO;
    }

    @Override
    public void modify(AddressDTO addressDTO) {
        Optional<Address> result = addressRepository.findById(addressDTO.getA_no());
        Address address = result.orElseThrow();
        address.change(addressDTO.getA_recipient(), addressDTO.getA_nickName(), addressDTO.getA_phone(), addressDTO.getA_zipCode(), addressDTO.getA_address(), addressDTO.getA_detail(), addressDTO.getA_basic());
        address.changeRequest(addressDTO.getA_request(), addressDTO.getA_customRequest());
        addressRepository.save(address);
    }

    @Override
    public void remove(Long a_no) {
        // Orders가 참조중이여서 삭제가 안된다.
        // 이를 해결하기 위해 참조중인 Orders들을 모두 가져와 해당 member의 기본배송지로 변경해준다.
        Optional<Address> result = addressRepository.findById(a_no);
        Address address = result.orElseThrow();
        // 해당 address를 참조하는 모든 ordersList
        List<Orders> ordersList = ordersRepository.addressRemoveSelect(address);
        addressRepository.deleteById(a_no);

    }

    @Override
    public List<AddressDTO> getList(String member) {
        List<Address> result = addressRepository.memberAddressList(member);
        List<AddressDTO> dtoList = new ArrayList<>();
        if(result != null){
            result.forEach(address -> {
                dtoList.add(entityToDTO(address));
            });
        }
        return dtoList;
    }
}
