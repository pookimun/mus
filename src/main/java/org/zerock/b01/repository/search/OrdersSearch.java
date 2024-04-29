package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.dto.OrdersListDTO;
import org.zerock.b01.dto.OrdersListDTO;

public interface OrdersSearch {
    // 주문내역 조회 시 기간, 상품명&브랜드명으로 검색 기능
    // 페이징 기능
    // order_detail도 같이 가져온다.

    Page<OrdersListDTO> searchWithAll(String keyword, Pageable pageable);
}
