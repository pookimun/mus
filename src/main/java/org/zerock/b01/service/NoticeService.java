package org.zerock.b01.service;

import org.zerock.b01.domain.Notice;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface NoticeService {

    Long register(NoticeDTO noticeDTO);

    NoticeDTO readOne(Long nno);

    void modify(NoticeDTO noticeDTO);

    void remove(Long nno);

    PageResponseDTO<NoticeListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Notice dtoToEntity(NoticeDTO noticeDTO){
        Notice notice = Notice.builder()
                .nno(noticeDTO.getNno())
                .n_title(noticeDTO.getN_title())
                .n_content(noticeDTO.getN_content())
                .n_writer(noticeDTO.getN_writer())
                .build();

        if(noticeDTO.getFileNames() != null){
            noticeDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                notice.addImage(arr[0], arr[1]);
            });
        }
        return notice;
    }

    default NoticeDTO entityToDTO(Notice notice) {

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .nno(notice.getNno())
                .n_title(notice.getN_title())
                .n_content(notice.getN_content())
                .n_writer(notice.getN_writer())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();

        List<String> fileNames =
                notice.getImageSet().stream().sorted().map(noticeImage ->
                        noticeImage.getUuid()+"_"+noticeImage.getFileName()).collect(Collectors.toList());
        noticeDTO.setFileNames(fileNames);

        return noticeDTO;
    }

}
