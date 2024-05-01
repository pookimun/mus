package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.Address;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.BoardService;
import org.zerock.b01.service.OrdersService;

import java.util.List;

@Controller
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final AddressService addressService;

    @GetMapping( "/orders")
    public void orders(PageRequestDTO pageRequestDTO, Model model){

//        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO =
//                boardService.listWithAll(pageRequestDTO);
//
//        log.info(responseDTO);
//
//        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/address/list") // 배송지 선택
    public void addressList(@Param("member") String member, Model model){
        List<AddressDTO> result = addressService.getList(member);
        log.info(result);
        model.addAttribute("addressDTOList", result);
        int resultCount = addressService.ListCount(member);
        model.addAttribute("addressCount", resultCount);
    }

    @GetMapping("/address/register") // 신규 배송지 추가
    public void addressRegister(){

    }

    @PostMapping("/address/register")
    public String addressRegisterPost(@Valid AddressDTO addressDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) {
            log.info("@Vaild 에러 !! " + bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/orders/address/register";
        }
        log.info(addressDTO);

        Long a_no = addressService.register(addressDTO);
        if(a_no != null){ // save에 성공했다면
            redirectAttributes.addFlashAttribute("registerResult", "registed");
        } else {
            redirectAttributes.addFlashAttribute("registerResult", "faild");
        }


        return "redirect:/orders/address/register";
        // register 성공 시 현재 창을 닫고 부모 창을 reload 한다.
    }

    @GetMapping("/address/modify")
    public void addressModify(@Param("ano") Long ano, Model model){ // 배송지 수정
        log.info("ano : " + ano);
        AddressDTO addressDTO = addressService.readOne(ano);
        log.info(addressDTO);
        model.addAttribute("dto", addressDTO);
    }

    @PostMapping("/address/modify")
    public String addressModifyPost(@Valid AddressDTO addressDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes){
        log.info("배송지 수정값 : " + addressDTO);
        if(bindingResult.hasErrors()) {
            log.info("@Vaild 에러 !! " + bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/orders/address/modify?ano="+addressDTO.getA_no();
        }
        addressService.modify(addressDTO);
        redirectAttributes.addFlashAttribute("modifyResult", "modified");
        redirectAttributes.addFlashAttribute("a_no", addressDTO.getA_no());
        return "redirect:/orders/address/modify?ano="+addressDTO.getA_no();
    }

    @PostMapping("/address/remove")
    public String addressRemove(AddressDTO addressDTO, RedirectAttributes redirectAttributes){
        Long a_no = addressDTO.getA_no();
        log.info("삭제할 배송지번호, 멤버, 배송지별칭 : " + addressDTO);
        addressService.remove(a_no);
        redirectAttributes.addFlashAttribute("removeResult", "removed");
        return "redirect:/orders/address/list?member=" + addressDTO.getMember();
    }

    @PostMapping("/address/read")
    public String addressRead(AddressDTO addressDTO, RedirectAttributes redirectAttributes){
        log.info(addressDTO);
        Long a_no = addressDTO.getA_no();
        AddressDTO readAddressDTO = addressService.readOne(a_no);
        log.info("읽어온 addressDTO : " + readAddressDTO); // a_use가 0으로 뜸 ...
        redirectAttributes.addAttribute("addressDTO", readAddressDTO);
        return null; // 값을 가지고 주문서로 이동 !
    }


}
