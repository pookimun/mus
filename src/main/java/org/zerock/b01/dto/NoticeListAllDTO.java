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
public class NoticeListAllDTO {

    private Long nno;

    private String n_title;

    private String n_writer;

    private LocalDateTime regDate;

//    private Long replyCount;

    private List<NoticeImageDTO> noticeImages;

}
