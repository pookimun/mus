package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.dto.KakaoApproveResponseDTO;
import org.zerock.b01.dto.KakaoReadyResponseDTO;
import org.zerock.b01.dto.OrdersListDTO;
import org.zerock.b01.repository.OrdersRepository;

import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class KakaoPayServiceImpl implements KakaoPayService {

    static final String cid = "TC0ONETIME"; // 가맹점 테스트용 코드
    static final String admin_Key = "b90607a5cba3ae5145370c1d1fbfd088"; // 커밋 시 빼고 올리기 !!!
    private KakaoReadyResponseDTO kakaoReady;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;

    @Transactional
    public KakaoReadyResponseDTO kakaoPayReady(Long ono) {
        log.info("kakaoPayReady 실행 !!!!! : " + ono);
        OrdersListDTO ordersListDTO = ordersService.readOne(ono);
        log.info("카카오 결제 !!! " + ordersListDTO);

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", ordersListDTO.getO_ordersno()); // 가맹점 주문 번호
        parameters.add("partner_user_id", ordersListDTO.getMember()); // 가맹점 회원 ID
        parameters.add("item_name", ordersListDTO.getOrdersDetailDTOList().get(0).getItemListAllDTO().getI_name() + "..."); // 상품명
        parameters.add("quantity", "2"); // 주문 수량
        parameters.add("total_amount", Integer.toString(ordersListDTO.getTotalPrice())); // 총 금액
        parameters.add("vat_amount", "0"); // 부과세
        parameters.add("tax_free_amount", "0"); // 상품 비과세 금액
        parameters.add("approval_url", "http://localhost:8008/payment/success/" + ordersListDTO.getOno()); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:8008/payment/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:8008/payment/fail"); // 실패 시 redirect url

        log.info(parameters);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        log.info(requestEntity);

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponseDTO.class);

        log.info(kakaoReady);

        return kakaoReady;
    }

    // 결제 완료 승인
    public KakaoApproveResponseDTO approveResponse(String pgToken, Long ono) {
        log.info("approveResponse 서비스 메서드 실행 ... ono : " + ono);

        OrdersListDTO ordersListDTO = ordersService.readOne(ono);

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", ordersListDTO.getO_ordersno());
        parameters.add("partner_user_id", ordersListDTO.getMember());
        parameters.add("pg_token", pgToken);

        log.info(parameters);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        log.info(requestEntity);

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponseDTO approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponseDTO.class);
        log.info(approveResponse);
        return approveResponse;
    }



    // 카카오 요구 헤더값
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }


}