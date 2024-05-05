package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.KakaoApproveResponseDTO;
import org.zerock.b01.dto.KakaoReadyResponseDTO;
import org.zerock.b01.service.KakaoPayService;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Log4j2
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;


    // 결제 요청
    @PostMapping("/ready")
    public KakaoReadyResponseDTO readyToKakaoPay() {
        return kakaoPayService.kakaoPayReady();
    }


    //  결제 성공
    @GetMapping("/success") // http://localhost/payment/success?pg_token=2070bd77435b54fa2315
    public ResponseEntity<KakaoApproveResponseDTO> afterPayRequest(@RequestParam("pg_token") String pgToken) {
        log.info("결제 성공 컨트롤러 실행 ... ");
        KakaoApproveResponseDTO kakaoApprove = kakaoPayService.approveResponse(pgToken);
        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }


}