package org.zerock.b01.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersRestController {

    private final AddressService addressService;

    @Operation(summary = "GET 방식으로 기본배송지 여부 조회")
    @GetMapping(value = "/addressDefault/{member}")
    public Map<String,Address> defaultAddressCheck(@PathVariable("member") String member) {
        log.info(member);

        Map<String, Address> resultMap = new HashMap<>();
        // 기본배송지 여부를 확인하고 존재하면 1, 존재하지 않으면 0을 리턴
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

//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/list/{bno}")
//    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
//                                             PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }
//
//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/{rno}")
//    public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = replyService.read(rno);
//
//        return replyDTO;
//    }
//
//    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
//    @DeleteMapping("/{rno}")
//    public Map<String,Long> remove( @PathVariable("rno") Long rno ){
//
//        replyService.remove(rno);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//
//
//


}
