package org.zerock.b01.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.CartDTO;
import org.zerock.b01.dto.CartDetailDTO;
import org.zerock.b01.service.CartService;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity
    order(@RequestBody @Valid CartDetailDTO cartDetailDTO, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String mid = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartDetailDTO, mid);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart") // http://localhost/cart
    public String orderHist(Principal principal, Model model) {
        List<CartDTO> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    }

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
        if(count <= 0) { // 장바구니 상품 개수가 0개 이하로 요청할 경우
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
            // 메세지와 함께 400 ERROR(BAD_REQUEST) 리턴
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())) {
            // 수정 권한 확인하여 true가 아니라면 -> 현재 로그인한 회원과 장바구니 상품 저장 회원이 다르다면
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
            // 메세지와 함께 403 ERROR(FORBIDDEN) 리턴
        }

        cartService.updateCartItemCount(cartItemId, count); // 장바구니 상품 개수 업데이트
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        // 메세지와 함께 요청 성공 HTTP 응답 상태 코드 리턴
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    // @DeleteMapping : 서버에서 특정 리소스를 삭제할 때 사용
    public @ResponseBody ResponseEntity deleteCartItem (@PathVariable("cartItemId") Long cartItemId,
                                                        Principal principal) {
        if(!cartService.validateCartItem(cartItemId, principal.getName())) {
            // 수정 권한 확인하여 true가 아니라면 -> 현재 로그인한 회원과 장바구니 상품 저장 회원이 다르다면
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
            // 메세지와 함께 403 ERROR(FORBIDDEN) 리턴
        }
        cartService.deleteCartItem(cartItemId); // 장바구니 상품 삭제
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        // 메세지와 함께 요청 성공 HTTP 응답 상태 코드 리턴
    }





}
