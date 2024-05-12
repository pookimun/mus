package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Item;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.ItemListAllDTO;

import java.util.List;

public interface ItemSearch {
    Page<Item> search1(Pageable pageable);
    Page<Item> searchAll(String[] types,String keyword,Pageable pageable);

    Page<ItemListAllDTO> searchWithAll(String[] types,
                                                 String keyword,
                                                 Pageable pageable);

//    List<ItemListAllDTO> selectItemListAllDTO(Long ino);

}
