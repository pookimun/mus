package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.service.BoardService;
import org.zerock.b01.service.OrdersService;

@Controller
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

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

}
