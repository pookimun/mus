package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Address;
import org.zerock.b01.domain.Orders;
import org.zerock.b01.repository.search.OrdersSearch;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersSearch {
    // c, r, d가 가능해야함

    @EntityGraph(attributePaths = {"ordersDetailSet"})
    // @EntityGraph에는 attributePaths라는 속성을 이용해서 같이 로딩해야 하는 속성 명시
    @Query("select o from Orders o where o.ono = :ono")
    Optional<Orders> findByIdWithOrdersDetails(Long ono);

}
