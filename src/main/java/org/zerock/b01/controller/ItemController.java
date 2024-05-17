package org.zerock.b01.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.ItemService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping({"/item","/admin"})
@Log4j2
@RequiredArgsConstructor
public class ItemController {

    private String uploadPath;

    private final ItemService itemService;
    @GetMapping("/list")
    public void list(ItemPageRequestDTO itemPageRequestDTO, Model model){


     //   ItemPageResponseDTO<ItemDTO> itemDTO = itemService.list(itemPageRequestDTO);

      ItemPageResponseDTO<ItemListAllDTO> itemDTO =
                itemService.listWithAll(itemPageRequestDTO);

        log.info(itemDTO);

        model.addAttribute("itemDTO", itemDTO);


    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid ItemDTO itemDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("아이템 POST register.......");

        if(bindingResult.hasErrors()) { // 오류발생시 addFlashAttribute로 1회용 에러 메시지를 담고 전달한다.
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/admin/list";
        }

        log.info(itemDTO);
        Long ino  = itemService.register(itemDTO);

        redirectAttributes.addFlashAttribute("result", ino);
        // 정상 처리시 addFlashAttribute에 결과 정보를 bno를 담아 전달한다.
        return "redirect:/admin/list";
    }
    @GetMapping({"/read","modify"})
    public void read(@Param("ino") Long ino, ItemPageRequestDTO itemPageRequestDTO, Model model){

        ItemDTO itemDTO = itemService.readOne(ino);
        log.info("여기는 컨트롤러"+itemDTO);

        List<String> fileNames = itemDTO.getFileNames();
        model.addAttribute("titleIMG", fileNames.get(0));
        log.info(fileNames.get(0));
        if (fileNames.size() > 1) { // 상세이미지가 있다면
            List<String> infoIMG = fileNames.subList(1, fileNames.size());
            model.addAttribute("infoIMG", infoIMG);
            log.info(infoIMG);
        }
        model.addAttribute("itemDTO", itemDTO);

    }
    @PostMapping("/modify")
    public String modify(ItemPageRequestDTO itemPageRequestDTO,
                         @Valid ItemDTO itemDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("아이템 modify post......." + itemDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = itemPageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            redirectAttributes.addAttribute("ino", itemDTO.getIno());

            return "redirect:/admin/modify?"+link;
        }

        itemService.modify(itemDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("ino", itemDTO.getIno());

        return "redirect:/admin/read";
    }

//
//    @PostMapping("/remove")
//    public String remove(Long ino, RedirectAttributes redirectAttributes) {
//
//        log.info("remove post.. " + ino);
//
//        itemService.remove(ino);
//
//        redirectAttributes.addFlashAttribute("result", "removed");
//
//        return "redirect:/admin/list";
//
//    }



    @PostMapping("/remove")
    public String remove(ItemDTO itemDTO, RedirectAttributes redirectAttributes) {

        Long ino  = itemDTO.getIno();
        log.info("remove post 지우기 " + ino);

        itemService.remove(ino);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(itemDTO.getFileNames());
        List<String> fileNames = itemDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/admin/list";

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





