package org.zerock.b01.service;

import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface AddressService {
    Long register(AddressDTO addressDTO);
    Long defaultAddress(Address address);
    // 기본배송지를 찾고 변경, 유지 작업을 하기 위한 메서드
    // ServiceImpl 내부에서 동작하는 메서드이기 때문에 엔티티를 받는다.
    AddressDTO readOne(Long a_no);
    void modify(AddressDTO addressDTO);
    void remove(Long a_no);

    List<AddressDTO> getList(String member);

    int ListCount(String member); // 배송지 개수 제한(5개)을 위해 배송지 개수를 가져온다.



    default Address dtoToEntity(AddressDTO addressDTO){
        Address address = Address.builder()
                .a_no(addressDTO.getA_no())
                .a_recipient(addressDTO.getA_recipient())
                .a_nickName(addressDTO.getA_nickName())
                .a_phone(addressDTO.getA_phone())
                .a_zipCode(addressDTO.getA_zipCode())
                .a_address(addressDTO.getA_address())
                .a_detail(addressDTO.getA_detail())
                .a_basic(addressDTO.getA_basic())
                .a_request(addressDTO.getA_request())
                .a_customRequest(addressDTO.getA_customRequest())
                .member(addressDTO.getMember())
                .a_use(addressDTO.getA_use())
                .build();
        return address;
    }

    default AddressDTO entityToDTO(Address address) {
        AddressDTO addressDTO = AddressDTO.builder()
                .a_no(address.getA_no())
                .a_recipient(address.getA_recipient())
                .a_nickName(address.getA_nickName())
                .a_phone(address.getA_phone())
                .a_zipCode(address.getA_zipCode())
                .a_address(address.getA_address())
                .a_detail(address.getA_detail())
                .a_basic(address.getA_basic())
                .a_request(address.getA_request())
                .a_customRequest(address.getA_customRequest())
                .member(address.getMember())
                .a_use(address.getA_use())
                .build();
        return addressDTO;
    }

}
