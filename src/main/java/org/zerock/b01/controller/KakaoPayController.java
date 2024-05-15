package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.*;

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
    private final CartService cartService;
    private final MemberService memberService;


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
                    .itemListAllDTO(itemService.selectItemListAllDTO(inos[i]))
                    .od_count(ordersDTO.getCounts()[i])
                    .od_size(ordersDTO.getSizes()[i])
                    .od_color(ordersDTO.getColors()[i])
                    .od_price(itemService.readOne(inos[i]).getI_price()) // 구매당시 가격저장
                    .cdid(ordersDTO.getCdids()[i])
                    .build();
            log.info("ordersDetailDTO : " + ordersDetailDTO);
            ordersDetailDTOList.add(ordersDetailDTO);
        }
        log.info("ordersDetailDTOList : " + ordersDetailDTOList);
        ordersListDTO.setOrdersDetailDTOList(ordersDetailDTOList);
        Long ono = ordersService.register(ordersListDTO); // 저장
        return kakaoPayService.kakaoPayReady(ono);
    }


    //  결제 성공
    @GetMapping("/success/{ono}") // http://localhost/payment/success/7?pg_token=2070bd77435b54fa2315
    public RedirectView afterPayRequest(@RequestParam("pg_token") String pgToken, @PathVariable("ono") Long ono) {
        log.info("결제 성공 컨트롤러 실행 ... ono : " + ono);
        // 결제 성공 시 orders, ordersDetail 테이블에 데이터 저장하기
        KakaoApproveResponseDTO kakaoApprove = kakaoPayService.approveResponse(pgToken, ono);
        log.info("kakaoApprove : " + kakaoApprove);
        ordersService.paymentSuccess(ono); // 결제성공으로 update

        // 멤버 포인트 변화 금액
        int point = 0; // 초기값 지정

        OrdersListDTO ordersListDTO = ordersService.readOne(ono);
        for (OrdersDetailDTO ordersDetailDTO : ordersListDTO.getOrdersDetailDTOList()){
            // 결제완료 된 CartDetail의 paymentSuccess 필드 값 1(결제완료)로 수정
            cartService.cdidPaymentSuccess(ordersDetailDTO.getCdid());
            if(ordersListDTO.getPointFirstUse() == 0) {
                // 적립금 선할인을 사용하지 않았을 때에만 상품 예상 적립금 point 변수에 저장
                point += (ordersDetailDTO.getOd_count() * ordersDetailDTO.getOd_price() * 0.005); // 적립예정 포인트 누적저장
            }
        }
        log.info("상품들의 적립금액 : " + point);

        // 포인트 사용값 만큼 보유 보인트에서 빼기
        point -= ordersListDTO.getPointUse();
        log.info("멤버 포인트 최종 변화 금액 : " + point);

        // 포인트 업뎃
        memberService.changePoint(kakaoApprove.getPartner_user_id(), point);

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