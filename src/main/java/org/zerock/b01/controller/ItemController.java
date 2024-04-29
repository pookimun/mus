package org.zerock.b01.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.ItemDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.service.ItemService;

@Controller
@RequestMapping({"/","/admin"})
@Log4j2
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @GetMapping("/admin")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<ItemDTO> itemDTO = itemService.list(pageRequestDTO);
        log.info(itemDTO);
        model.addAttribute("itemDTO", itemDTO); //값


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
            return "redirect:/item/register";
        }

        log.info(itemDTO);

        Long ino  = itemService.register(itemDTO);

        redirectAttributes.addFlashAttribute("result", ino);
        // 정상 처리시 addFlashAttribute에 결과 정보를 bno를 담아 전달한다.
        return "redirect:/item/list";
    }

}


