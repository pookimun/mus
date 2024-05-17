package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardImageRepository;
import org.zerock.b01.repository.BoardRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

    private final BoardImageRepository boardImageRepository;


    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = dtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();

        if (boardDTO.getFiles() != null && !boardDTO.getFiles().isEmpty()) {
            for (int i = 0; i < boardDTO.getFiles().size(); i++) {
                MultipartFile file = boardDTO.getFiles().get(i);
                if (!file.isEmpty()) {
                    saveFile(bno, file, i);
                }
            }
        }


        return bno;
    }

    private void saveFile(Long bno, MultipartFile file, int i) {
        try {
            String uploadDir = "uploads/" + bno;
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 정보를 데이터베이스에 저장
            BoardImage boardImage = BoardImage.builder()
                    .uuid(uuid)
                    .fileName(file.getOriginalFilename())
                    .board(boardRepository.findById(bno).orElseThrow())
                    .build();

            boardImageRepository.save(boardImage);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }




    @Override
    public BoardDTO readOne(Long bno) {

        //board_image까지 조인 처리되는 findByWithImages()를 이용
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일의 처리
        board.clearImages();

        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }



    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());


        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


}
