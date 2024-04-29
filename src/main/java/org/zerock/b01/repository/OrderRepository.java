package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // c, r, d가 가능해야함
}
