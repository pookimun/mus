package org.zerock.b01.service;

import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface AddressService {
    Long register(AddressDTO addressDTO);
    AddressDTO readOne(Long a_no);
    void modify(AddressDTO addressDTO);
    void remove(Long a_no);

    List<AddressDTO> getList(String member);

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
                .member(addressDTO.getMember())
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
                .member(address.getMember())
                .build();
        return addressDTO;
    }

}
