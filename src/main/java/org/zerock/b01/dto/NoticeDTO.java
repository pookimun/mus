package org.zerock.b01.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Long nno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String n_title;

    @NotEmpty
    private String n_content;

    @NotEmpty
    private String n_writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    //첨부파일의 이름들
    private List<String> fileNames;
}
