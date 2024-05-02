package org.zerock.b01.service;

import org.zerock.b01.dto.*;

public interface ItemService {
    Long register(ItemDTO itemDTO);
    ItemDTO readOne(Long ino);
    void modify(ItemDTO itemDTO);
    void remove(Long ino);

    ItemPageResponseDTO<ItemDTO> list(ItemPageRequestDTO itemPageRequestDTO);
}
