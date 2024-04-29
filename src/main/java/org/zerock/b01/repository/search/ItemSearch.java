package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Item;

public interface ItemSearch {
    Page<Item> search1(Pageable pageable);
    Page<Item> searchAll(String[] types,String keyword,Pageable pageable);

}
