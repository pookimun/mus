package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemListAllDTO {

    private Long ino;

    private String i_name;

    private String i_color; //i_price 에서 변경

    private LocalDateTime regDate;

    private Long replyCount;


    private List<ItemImageDTO> itemImages;

}
