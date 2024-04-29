package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Orders;


public interface ItemRepository extends JpaRepository<Item, Long> {

}
