package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Order;


public interface ItemRepository extends JpaRepository<Item, Long> {

}
