package org.zerock.b01.service;

import org.zerock.b01.dto.KakaoApproveResponseDTO;
import org.zerock.b01.dto.KakaoReadyResponseDTO;
import org.zerock.b01.dto.OrdersListDTO;


public interface KakaoPayService {

    // 결제 요청
    KakaoReadyResponseDTO kakaoPayReady(Long ono);

    // 결제 완료 승인
    KakaoApproveResponseDTO approveResponse(String pgToken, Long ono);



}