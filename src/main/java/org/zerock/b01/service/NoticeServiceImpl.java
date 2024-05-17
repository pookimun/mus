package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Notice;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.repository.NoticeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final ModelMapper modelMapper;

    private final NoticeRepository noticeRepository;

    @Override
    public Long register(NoticeDTO noticeDTO) {
        Notice notice = dtoToEntity(noticeDTO);
        Long nno = noticeRepository.save(notice).getNno();
        return nno;
    }

    @Override
    public NoticeDTO readOne(Long nno) {
        Optional<Notice> result = noticeRepository.findByIdWithImages(nno);
        Notice notice = result.orElseThrow();
        NoticeDTO noticeDTO = entityToDTO(notice);
        return noticeDTO;
    }

    @Override
    public void modify(NoticeDTO noticeDTO) {
        Optional<Notice> result = noticeRepository.findById(noticeDTO.getNno());
        Notice notice = result.orElseThrow();
        // 제목, 내용 변경
        notice.change(noticeDTO.getN_title(), noticeDTO.getN_content());
        //첨부파일의 처리
        notice.clearImages();

        if(noticeDTO.getFileNames() != null){
            for (String fileName : noticeDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                notice.addImage(arr[0], arr[1]);
            }
        }
        noticeRepository.save(notice);
    }

    @Override
    public void remove(Long nno) {
        noticeRepository.deleteById(nno);
    }

    @Override
    public PageResponseDTO<NoticeListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("nno");

        Page<NoticeListAllDTO> result = noticeRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<NoticeListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


}
