package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Item;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.ItemListAllDTO;

public interface ItemSearch {
    Page<Item> search1(Pageable pageable);
    Page<Item> searchAll(String[] types,String keyword,Pageable pageable);

    Page<ItemListAllDTO> searchWithAll(String[] types,
                                                 String keyword,
                                                 Pageable pageable);

}
