package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Item;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface ItemService {
    Long register(ItemDTO itemDTO);
    ItemDTO readOne(Long ino);
    void modify(ItemDTO itemDTO);
    void remove(Long ino);

    ItemPageResponseDTO<ItemDTO> list(ItemPageRequestDTO itemPageRequestDTO);

    //게시글의 이미지 처리
    ItemPageResponseDTO<ItemListAllDTO> listWithAll(ItemPageRequestDTO itemPageRequestDTO);

    // 주문목록 list에서 상품 이미지 하나를 출력할 수 있도록 함
    ItemListAllDTO selectItemListAllDTO(Long ino);


    // 성은 : item 테이블의 itemDetail 필드는 뺀건지 ! 뺀게 아니라면 아래 두 메서드에 추가해야함
    default Item dtoToEntity(ItemDTO itemDTO){

        Item item = Item.builder()
                .ino(itemDTO.getIno())
                .i_name(itemDTO.getI_name())
                .i_color(itemDTO.getI_color())
                .i_size(itemDTO.getI_size())
                .i_price(itemDTO.getI_price())
                .i_stock(itemDTO.getI_stock())
                .build();

        if(itemDTO.getFileNames() != null){
            itemDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                item.addImage(arr[0], arr[1]);
            });
        }
        return item;
    }

    default ItemDTO entityToDTO(Item item) {

        ItemDTO itemDTO = ItemDTO.builder()
                .ino(item.getIno())
                .i_name(item.getI_name())
                .i_color(item.getI_color())
                .i_size(item.getI_size())
                .i_price(item.getI_price())
                .i_stock(item.getI_stock())
                .build();

        List<String> fileNames =
                item.getItemImageSet().stream().sorted().map(itemImage ->
                        itemImage.getUuid()+"_"+itemImage.getFileName()).collect(Collectors.toList());

        itemDTO.setFileNames(fileNames);

        return itemDTO;
    }


}