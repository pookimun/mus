package org.zerock.b01.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.zerock.b01.domain.Item;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService{
    private final ModelMapper modelMapper;

    private final ItemRepository itemRepository;

    @Override
    public Long register(ItemDTO itemDTO){
        Item item = modelMapper.map(itemDTO, Item.class);
        return itemRepository.save(item).getIno();
    }
    /*asd*/

    @Override
    public ItemDTO readOne(Long ino) {


       Optional<Item> result = itemRepository.findById(ino);

        Item item = result.orElseThrow();

       ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);

       return itemDTO;
    }

    @Override
    public void modify(ItemDTO itemDTO) {

        Optional<Item> result = itemRepository.findById(itemDTO.getIno());

        Item item = result.orElseThrow();

        item.change(itemDTO.getI_name(),itemDTO.getI_price(),itemDTO.getI_color(),itemDTO.getI_size());

        itemRepository.save(item);
    }

    @Override
    public void remove(Long ino) {
        itemRepository.deleteById(ino);
    }

    @Override
    public ItemPageResponseDTO<ItemDTO> list(ItemPageRequestDTO itemPageRequestDTO) {
        String[] types = itemPageRequestDTO.getTypes();
        String keyword = itemPageRequestDTO.getKeyword();
        Pageable pageable = itemPageRequestDTO.getPageable("ino");

        Page<Item> result = itemRepository.searchAll(types, keyword, pageable);

        List<ItemDTO> dtoList = result.getContent().stream()
                .map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());


        return ItemPageResponseDTO.<ItemDTO>withAll()
                .itemPageRequestDTO(itemPageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();

    }

}
