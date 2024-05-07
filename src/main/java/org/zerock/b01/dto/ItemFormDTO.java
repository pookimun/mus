package org.zerock.b01.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.zerock.b01.constant.ItemSellStatus;
import org.zerock.b01.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String i_name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer i_price;

    @NotBlank(message = "상품 정보는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer i_stock;

    private ItemSellStatus itemSellStatus;

    private List<ItemImageDTO> itemImgDtoList = new ArrayList<>();
    // 상품 저장 후 수정 시 이미지 정보 저장 리스트

    private List<Long> itemImgIds = new ArrayList<>();
    // 상품 이미지 아이디 저장 리스트 -> 수정 시 이미지 아이디 담는 용도

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() { // DTO -> Entity
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDTO of(Item item) { // Entity -> DTO
        return modelMapper.map(item, ItemFormDTO.class);
    }

}
