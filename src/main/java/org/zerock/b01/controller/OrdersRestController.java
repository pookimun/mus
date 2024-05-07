package org.zerock.b01.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.domain.Address;
import org.zerock.b01.dto.AddressDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.ReplyService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersRestController {

    private final AddressService addressService;

    @Operation(summary = "멤버의 기본배송지를 가져와 리턴")
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/addressDefault")
    public Map<String,Address> defaultAddressCheck(Principal principal) {
        String member = principal.getName();
        log.info("기본배송지 확인" + member);

        Map<String, Address> resultMap = new HashMap<>();
        // 기본배송지 여부를 확인하고 존재하면 Address를, 존재하지 않으면 null을 리턴
        Address defAddress = addressService.defaultAddressCheck(member);
        resultMap.put("defAddress",defAddress);
        log.info(resultMap);
        return resultMap;
    }

    @Operation(summary =  "기존의 기본배송지의 a_basic 필드 값을 0으로 수정")
    @PutMapping(value = "/addressDefault/{ano}")
    public Map<String,String> defaultAddressfalse( @PathVariable("ano") Long ano){
        AddressDTO defAddressDTO = addressService.readOne(ano);
        defAddressDTO.setA_basic(0); // 기본배송지의 a_basic 필드 값 0으로 수정
        addressService.modify(defAddressDTO);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("defaultAddressfalse", "defaultAddressfalse");
        return resultMap;
    }

    @Operation(summary = "멤버의 배송지 리스트 리턴")
    @GetMapping(value = "/addressList")
    public Map<String, List<AddressDTO>> AddressList(Principal principal) {
        String member = principal.getName();
        log.info("배송지 리스트 리턴" + member);

        Map<String, List<AddressDTO>> resultMap = new HashMap<>();
        // 멤버의 배송지 리스트를 찾아온다.
        List<AddressDTO> addressDTOS = addressService.getList(member);
        resultMap.put("addressDTOS", addressDTOS);
        log.info(resultMap);
        return resultMap;
    }

    @Operation(summary = "배송지 번호를 받아 배송지를 리턴")
    @GetMapping("/address/{ano}")
    public AddressDTO getAddress( @PathVariable("ano") Long ano ){
        AddressDTO addressDTO = addressService.readOne(ano);
        return addressDTO;
    }



}
