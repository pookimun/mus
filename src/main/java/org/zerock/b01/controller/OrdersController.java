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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.Address;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.BoardService;
import org.zerock.b01.service.MemberService;
import org.zerock.b01.service.OrdersService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final AddressService addressService;
    private final MemberService memberService;

    @PreAuthorize("permitAll()")
    @GetMapping( "/orders") // 주문서
    public void orders(Principal principal, OrdersPageRequestDTO ordersPageRequestDTO, Model model){ // , Long[] cnos
        log.info("orders 컨트롤러 실행 ... ");
//        for(Long cno : cnos){
//            // 여기 하는중 !!
//        }
        log.info(principal);
        MemberDTO memberDTO = memberService.readMember(principal.getName());
        log.info(memberDTO.getM_point());
        model.addAttribute("memberPoint", memberDTO.getM_point());
    }

    // 주문서에는 상품에서 정보가 넘어와서 출력이 되어야 하는데, 아직 어떻게 받을지 모르겠음 !!

//    log.info("orders 실행"); 주문내역 조회에서 사용 !
//    OrdersPageResponseDTO<OrdersListDTO> result = ordersService.listWithAll(member, ordersPageRequestDTO);
//        log.info(ordersPageRequestDTO);
//        log.info(result);
//
//        model.addAttribute("resultList", result);

    @PreAuthorize("permitAll()")
    @GetMapping("/address/list") // 배송지 선택
    public void addressList(Principal principal, Model model){
        log.info("addressList 컨트롤러 실행");
        String member = principal.getName();
        log.info(member);
        List<AddressDTO> result = addressService.getList(member);
        log.info(result);
        model.addAttribute("addressDTOList", result);
        int resultCount = addressService.ListCount(member);
        model.addAttribute("addressCount", resultCount); // 배송지 개수 제한을 위해 추가
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/address/register") // 신규 배송지 추가
    public void addressRegister(){

    }

    @PreAuthorize("permitAll()")
    @PostMapping("/address/register")
    public String addressRegisterPost(Principal principal, @Valid AddressDTO addressDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        String member = principal.getName();
        log.info("주소 등록 " + member);
        addressDTO.setMember(member);

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

    @PreAuthorize("permitAll()")
    @GetMapping("/address/modify")
    public void addressModify(@Param("ano") Long ano, Model model){ // 배송지 수정
        log.info("ano : " + ano);
        AddressDTO addressDTO = addressService.readOne(ano);
        log.info(addressDTO);
        model.addAttribute("dto", addressDTO);
    }

    @PreAuthorize("permitAll()")
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

    @PreAuthorize("permitAll()")
    @PostMapping("/address/remove")
    public String addressRemove(AddressDTO addressDTO, RedirectAttributes redirectAttributes){
        Long a_no = addressDTO.getA_no();
        log.info("삭제할 배송지번호, 멤버, 배송지별칭 : " + addressDTO);
        addressService.remove(a_no);
        redirectAttributes.addFlashAttribute("removeResult", "removed");
        return "redirect:/orders/address/list?member=" + addressDTO.getMember();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/address/read")
    public String addressRead(AddressDTO addressDTO, RedirectAttributes redirectAttributes){
        log.info(addressDTO);
        Long a_no = addressDTO.getA_no();
        AddressDTO readAddressDTO = addressService.readOne(a_no);
        log.info("읽어온 addressDTO : " + readAddressDTO); // a_use가 0으로 뜸 ...
        redirectAttributes.addAttribute("addressDTO", readAddressDTO);
        return null; // 값을 가지고 주문서로 이동 !
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/success")
    public void ordersSuccess(){

    }

}
