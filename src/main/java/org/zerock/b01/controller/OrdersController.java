package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b01.domain.Address;
import org.zerock.b01.dto.AddressDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
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

    @GetMapping("/address/list")
    public void addressList(@Param("member") String member, Model model){
        List<AddressDTO> result = addressService.getList(member);
        log.info(result);
        model.addAttribute("addressDTOList", result);
    }

    @GetMapping("/address/register")
    public void addressRegister(){

    }

}
