package org.zerock.b01.service;

import org.zerock.b01.dto.KakaoApproveResponseDTO;
import org.zerock.b01.dto.KakaoReadyResponseDTO;


public interface KakaoPayService {

    // 결제 요청
    KakaoReadyResponseDTO kakaoPayReady();

    // 결제 완료 승인
    KakaoApproveResponseDTO approveResponse(String pgToken);

}