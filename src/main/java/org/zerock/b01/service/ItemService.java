package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.ItemDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

public interface ItemService {
    Long register(ItemDTO itemDTO);
    ItemDTO readOne(Long ino);
    void modify(ItemDTO itemDTO);
    void remove(Long ino);

    PageResponseDTO<ItemDTO> list(PageRequestDTO pageRequestDTO);
}
