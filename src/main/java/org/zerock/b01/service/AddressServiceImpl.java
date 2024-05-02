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
        address.changeUse(1);
        // 배송지 작성 시 무조건 a_use 값은 1로 들어가게 함
        Long a_no = addressRepository.save(address).getA_no();
        return a_no;
    }

    @Override
    public Address defaultAddressCheck(String member) {
        // 기본배송지가 있는지 없는지 확인 후
        // 기본배송지가 있으면 : 저장하지않고 null 리턴
        // 기본배송지가 없으면 : 저장하고 저장된 a_no 값 리턴

        Address defAddress = null;
        Optional<Address> result = addressRepository.memberAddressBasic(member);
        if (!result.isEmpty()){ // 기본배송지가 있으면
            defAddress = result.orElseThrow();
        }
        return defAddress;
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
        Optional<Address> result = addressRepository.findById(a_no);
        Address address = result.orElseThrow();
        address.changeUse(0); // 해당 엔티티의 사용여부를 0으로 만든다.
        addressRepository.save(address); // update
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

    @Override
    public int ListCount(String member) {
        int result = addressRepository.memberAddressListCount(member);
        return result;
    }


}
