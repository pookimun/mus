package org.zerock.b01.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.domain.Address;
import org.zerock.b01.dto.AddressDTO;
import org.zerock.b01.dto.CartDetailDTO;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.CartService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;


    @Operation(summary = "장바구니 담기 클릭 시 장바구니 테이블에 저장")
    @PreAuthorize("permitAll()")
    @PostMapping(value = "/addCart", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addCart(@RequestBody @Valid CartDetailDTO cartDetailDTO, BindingResult bindingResult, Principal principal) {
        log.info("addCart 메서드 실행 : " + cartDetailDTO.toString());
        String mid = principal.getName();
        Long cdid;
        Map<String, String> resultMap = new HashMap<>();

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            resultMap.put("cartErrors",sb.toString());
            return resultMap;
        }

        try {
            cdid = cartService.addCart(cartDetailDTO, mid);
        } catch(Exception e){
            resultMap.put("serviceErrors",e.getMessage());
            return resultMap;
        }

        resultMap.put("cdid", cdid.toString());

        log.info(resultMap);
        return resultMap;
    }




}
