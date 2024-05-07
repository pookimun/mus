package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.ItemService;
import org.zerock.b01.service.KakaoPayService;
import org.zerock.b01.service.OrdersService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Log4j2
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    private final AddressService addressService;
    private final OrdersService ordersService;
    private final ItemService itemService;


    // 결제 요청
    @Transactional
    @PostMapping(value = "/ready", consumes = MediaType.APPLICATION_JSON_VALUE)
    public KakaoReadyResponseDTO readyToKakaoPay(Principal principal, @RequestBody OrdersDTO ordersDTO) {
        log.info("========================================================================================================================================================================================================");
        log.info(principal.getName());
        log.info("ordersDTO : " + ordersDTO);
        // 필요한 정보를 ordersDTO에 담아 매개값으로 받음
        OrdersListDTO ordersListDTO = OrdersListDTO.builder()
                .member(principal.getName())
                .addressDTO(addressService.readOne(ordersDTO.getAno()))
                .o_state(ordersDTO.getO_state())
                .totalPrice(ordersDTO.getTotalPrice())
                .pointFirstUse(ordersDTO.getPointFirstUse())
                .pointUse(ordersDTO.getPointUse())
                .paymentMethod(ordersDTO.getPaymentMethod())
                .cardCompany(ordersDTO.getCardCompany())
                .installment(ordersDTO.getInstallment())
                .build();
        log.info("ordersListDTO : " + ordersListDTO);
        Long[] inos = ordersDTO.getInos();
        List<OrdersDetailDTO> ordersDetailDTOList = new ArrayList<>();
        for (int i = 0; i < inos.length; i++) {
            OrdersDetailDTO ordersDetailDTO = OrdersDetailDTO.builder()
                    //.orders(ono)
                    .itemDTO(itemService.readOne(inos[i]))
                    .od_count(ordersDTO.getCounts()[i])
                    .od_size(ordersDTO.getSizes()[i])
                    .od_color(ordersDTO.getColors()[i])
                    .od_price(itemService.readOne(inos[i]).getI_price()) // 구매당시 가격저장
                    .build();
            log.info("ordersDetailDTO : " + ordersDetailDTO);
            ordersDetailDTOList.add(ordersDetailDTO);
        }
        log.info("ordersDetailDTOList : " + ordersDetailDTOList);
        ordersListDTO.setOrdersDetailDTOList(ordersDetailDTOList);
        Long ono = ordersService.register(ordersListDTO);
        return kakaoPayService.kakaoPayReady(ono);
    }


    //  결제 성공
    @GetMapping("/success/{ono}") // http://localhost/payment/success/7?pg_token=2070bd77435b54fa2315
    public RedirectView afterPayRequest(@RequestParam("pg_token") String pgToken, @PathVariable("ono") Long ono) {
        log.info("결제 성공 컨트롤러 실행 ... ono : " + ono);
        // 결제 성공 시 orders, ordersDetail 테이블에 데이터 저장하기
        KakaoApproveResponseDTO kakaoApprove = kakaoPayService.approveResponse(pgToken, ono);
        ordersService.paymentSuccess(ono); // 결제성공으로 update
        log.info("완료");
        // 리다이렉션을 위한 RedirectView 생성
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/orders/success"); // 리다이렉션할 URL 지정
        return redirectView;
    }

    @GetMapping("/cancel")
    public RedirectView cancle(){
        log.info("결제 취소 컨트롤러 실행 ... ");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/orders/cancel"); // 리다이렉션할 URL 지정
        return redirectView;
    }

    @GetMapping("/fail")
    public RedirectView fail(){
        log.info("결제 실패 컨트롤러 실행 ... ");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/orders/fail"); // 리다이렉션할 URL 지정
        return redirectView;
    }




}