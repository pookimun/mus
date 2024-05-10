package org.zerock.b01.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.zerock.b01.domain.ItemImg;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImageDTO {

    private Long id;

    private String uuid;

    private String fileName;

    private int ord;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDTO of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImageDTO.class);
    }
}
