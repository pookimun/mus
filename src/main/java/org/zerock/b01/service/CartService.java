package org.zerock.b01.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import org.zerock.b01.domain.Cart;
import org.zerock.b01.domain.CartDetail;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.CartDTO;
import org.zerock.b01.dto.CartDetailDTO;
import org.zerock.b01.repository.CartDetailRepository;
import org.zerock.b01.repository.CartRepository;
import org.zerock.b01.repository.ItemRepository;
import org.zerock.b01.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public Long addCart(CartDetailDTO cartDetailDTO, String mid) {


        Item item = itemRepository.findById(cartDetailDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByMidOnly(mid); // 현재 로그인한 회원 엔티티 조회하여 member로 저장

        Cart cart = cartRepository.findByMember_Mid(member.getMid()); // 현재 로그인한 회원의 장바구니 엔티티 조회하여 cart에 저장
        if(cart == null) { // cart가 비어있다면 -> 아직 장바구니가 생성되지 않음
            cart = Cart.createCart(member); // 장바구니 엔티티 생성
            cartRepository.save(cart);
        }

        CartDetail savedCartItem = cartDetailRepository.findByCart_CnoAndItem_Ino(cart.getCno(), item.getIno());
        // 현재 상품이 장바구니에 존재하는지 조회

        if(savedCartItem != null) { // savedCartItem이 null이 아니라면 -> 이미 장바구니에 존재하는 상품인 경우
            savedCartItem.addCount(cartDetailDTO.getCount()); // 장바구니 담을 수량만큼 add해줌
            return savedCartItem.getCdid();
        } else {
            CartDetail cartDetail = CartDetail.createCartDetail(cart, item, cartDetailDTO.getCount());
            // CartItem 엔티티 생성
            cartDetailRepository.save(cartDetail);
            return cartDetail.getCdid();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDTO> getCartList(String mid) {

        List<CartDTO> cartDTOList = new ArrayList<>();

        Member member = memberRepository.findByMidOnly(mid);
        Cart cart = cartRepository.findByMember_Mid(member.getMid());
        // 현재 로그인한 회원의 장바구니 엔티티 조회
        if(cart == null) { // 장바구니가 비어있다면
            return cartDTOList; // 비어있는 리스트 반환
        }

        cartDTOList = cartDetailRepository.findCartDetailDtoList(cart.getCno());
        return  cartDTOList;
        // 장바구니에 담긴 상품 정보 조회하여 리턴
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String mid) {
        Member curMember = memberRepository.findByMidOnly(mid); // 현재 로그인한 회원 조회
        CartDetail cartDetail = cartDetailRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartDetail.getCart().getMember(); // 장바구니 상품 저장 회원 조회

        if(!StringUtils.equals(curMember.getMid(), savedMember.getMid())) {
            // curMember와 savedMember가 같지 않다면
            return false; // false 리턴
        }
        return true; // 같다면 true 리턴
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartDetail cartDetail = cartDetailRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartDetail.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartDetail cartDetail = cartDetailRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartDetailRepository.delete(cartDetail);
    }




}