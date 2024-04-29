package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.domain.OrdersDetail;
import org.zerock.b01.repository.search.OrdersSearch;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>, OrdersSearch {
    // c, r, d가 가능해야함




}
