package org.zerock.b01.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.ItemService;

@Controller
@RequestMapping({"/","/admin"})
@Log4j2
@RequiredArgsConstructor
public class ItemController {

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
        log.info(itemDTO);


        model.addAttribute("itemDTO", itemDTO);

    }
    @PostMapping("/modify")
    public String modify(ItemPageRequestDTO itemPageRequestDTO,
                         @Valid ItemDTO itemDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("보드 modify post......." + itemDTO);

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


    @PostMapping("/remove")
    public String remove(Long ino, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + ino);

        itemService.remove(ino);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/admin/list";

    }



}


