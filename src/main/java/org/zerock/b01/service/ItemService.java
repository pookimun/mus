package org.zerock.b01.service;

import org.springframework.web.multipart.MultipartFile;
import org.zerock.b01.dto.*;

import java.util.List;

public interface ItemService {
    Long register(ItemDTO itemDTO);
    ItemDTO readOne(Long ino);
    void modify(ItemDTO itemDTO);
    void remove(Long ino);

    ItemPageResponseDTO<ItemDTO> list(ItemPageRequestDTO itemPageRequestDTO);




}
