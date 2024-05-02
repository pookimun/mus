package org.zerock.b01.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Cart;
import org.zerock.b01.domain.CartDetail;
import org.zerock.b01.domain.Item;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.CartDetailDTO;
import org.zerock.b01.repository.CartDetailRepository;
import org.zerock.b01.repository.CartRepository;
import org.zerock.b01.repository.ItemRepository;
import org.zerock.b01.repository.MemberRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartServiceImpl implements CartService{

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;



    @Override
    public Long addCart(CartDetailDTO cartDetailDTO, String mid) {

        if (mid == null) {
            throw new IllegalArgumentException("mid가 null입니다.");
        }

        Item item = itemRepository.findById(cartDetailDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Optional<Member> optionalMember = memberRepository.findByMid(mid);
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다 : " + mid));

        Cart cart = cartRepository.findByMember_Mid(mid);

        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartDetail savedCartItem = cartDetailRepository.findByCart_CnoAndItem_Ino(cart.getCno(), item.getIno());

        if(savedCartItem != null){
            savedCartItem.addCount(cartDetailDTO.getCount());
            return savedCartItem.getCdid();
        } else {
            CartDetail cartDetail = CartDetail.createCartDetail(cart, item, cartDetailDTO.getCount());
            cartDetailRepository.save(cartDetail);
            return cartDetail.getCdid();
        }
    }


}
