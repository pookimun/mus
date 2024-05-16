package org.zerock.b01.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.NoticeService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping({"/","/notice"})
@Log4j2
@RequiredArgsConstructor
public class NoticeController {

    @Value("${org.zerock.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;
    private final NoticeService noticeService;

    @GetMapping( "/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<NoticeListAllDTO> responseDTO = noticeService.listWithAll(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("notice POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/notice/register";
        }

        log.info(noticeDTO);

        Long nno  = noticeService.register(noticeDTO);

        redirectAttributes.addFlashAttribute("result", nno);

        return "redirect:/notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify"})
    public void read(Long nno, PageRequestDTO pageRequestDTO, Model model){
        NoticeDTO noticeDTO = noticeService.readOne(nno);
        log.info(noticeDTO);
        model.addAttribute("dto", noticeDTO);
    }

    @PreAuthorize("principal.username == #noticeDTO.n_writer")
    @PostMapping("/modify")
    public String modify( @Valid NoticeDTO noticeDTO,
                          BindingResult bindingResult,
                          PageRequestDTO pageRequestDTO,
                          RedirectAttributes redirectAttributes){
        log.info("notice modify post......." + noticeDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            redirectAttributes.addAttribute("nno", noticeDTO.getNno());
            return "redirect:/notice/modify?"+link;
        }
        noticeService.modify(noticeDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("nno", noticeDTO.getNno());

        return "redirect:/notice/read";
    }

    @PreAuthorize("principal.username == #noticeDTO.n_writer")
    @PostMapping("/remove")
    public String remove(NoticeDTO noticeDTO, RedirectAttributes redirectAttributes) {
        Long nno  = noticeDTO.getNno();
        log.info("remove post.. " + nno);
        noticeService.remove(nno);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(noticeDTO.getFileNames());
        List<String> fileNames = noticeDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/notice/list";
    }

    public void removeFiles(List<String> files){
        for (String fileName:files) {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                //섬네일이 존재한다면
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }//end for
    }
}
