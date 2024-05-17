package org.zerock.b01.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import org.zerock.b01.domain.*;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.CartDetailRepository;
import org.zerock.b01.repository.CartRepository;
import org.zerock.b01.repository.ItemRepository;
import org.zerock.b01.repository.MemberRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ItemService itemService;

    public Long addCart(CartDetailDTO cartDetailDTO, String mid) {
        System.out.println("addCart 서비스 메서드 cartDetailDTO" + cartDetailDTO);

        Item item = itemRepository.findById(cartDetailDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByMidOnly(mid); // 현재 로그인한 회원 엔티티 조회하여 member로 저장

        Cart cart = cartRepository.findByMember_Mid(member.getMid()); // 현재 로그인한 회원의 장바구니 엔티티 조회하여 cart에 저장
        if (cart == null) { // cart가 비어있다면 -> 아직 장바구니가 생성되지 않음
            cart = Cart.createCart(member); // 장바구니 엔티티 생성
            cartRepository.save(cart);
        }

        CartDetail savedCartItem = cartDetailRepository.findByCart_CnoAndItem_Ino(cart.getCno(), item.getIno());
        // 현재 상품이 장바구니에 존재하는지 조회

        if (savedCartItem != null) { // savedCartItem이 null이 아니라면 -> 이미 장바구니에 존재하는 상품인 경우
            savedCartItem.addCount(cartDetailDTO.getCount()); // 장바구니 담을 수량만큼 add해줌
            return savedCartItem.getCdid();
        } else {
            CartDetail cartDetail = CartDetail.createCartDetail(cart, item, cartDetailDTO.getCount(), cartDetailDTO.getSize(), cartDetailDTO.getColor());
            // CartItem 엔티티 생성
            cartDetailRepository.save(cartDetail);
            return cartDetail.getCdid();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDTO> getCartList(String mid) {
        System.out.println("service : getCartList 실행 ... " + mid);
        List<CartDTO> cartDTOList = new ArrayList<>();
        Member member = memberRepository.findByMidOnly(mid);
        Cart cart = cartRepository.findByMember_Mid(member.getMid());
        // 현재 로그인한 회원의 장바구니 엔티티 조회
        if (cart == null) { // 장바구니가 비어있다면
            return cartDTOList; // 비어있는 리스트 반환
        }

        List<CartDetail> cartDetails = cartDetailRepository.findByCartWherePaymentSuccess(cart);
        for(CartDetail cartDetail : cartDetails){
            Optional<ItemImage> result = cartDetail.getItem().getItemImageSet().stream()
                    .filter(itemImage -> itemImage.getOrd() == 0)
                    .findFirst();
            ItemImage itemImage = result.orElseThrow();
            CartDTO cartDTO = CartDTO.builder()
                    .cartItemId(cartDetail.getCdid())
                    .itemNm(cartDetail.getItem().getI_name())
                    .price(cartDetail.getItem().getI_price())
                    .count(cartDetail.getCount())
                    .fileName(itemImage.getFileName()) // 0번째가 대표이미지
                    .uuid(itemImage.getUuid())
                    .build();
            cartDTOList.add(cartDTO);
        }
        return cartDTOList;
        // 장바구니에 담긴 상품 정보 조회하여 리턴
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String mid) {
        Member curMember = memberRepository.findByMidOnly(mid); // 현재 로그인한 회원 조회
        CartDetail cartDetail = cartDetailRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartDetail.getCart().getMember(); // 장바구니 상품 저장 회원 조회

        if (!StringUtils.equals(curMember.getMid(), savedMember.getMid())) {
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

    // 성은추가
    // 장바구니에서 선택한 장바구니번호를 매개값으로 해당 장바구니 DTO를 리턴하는 메서드
    // 장바구니(Cart) 하나에 장바구니 상품별 정보(CartDetail) 여러개 = 1:N관계(일대다관계)
    // 회원(member) 한 명당 하나의 장바구니(Cart)를 가진다.
    // 선택상품구매 기능의 경우 ...........
    // 컨트롤러에서 장바구니(Cart)의 번호를 받는것이 아닌 장바구니 상품별 정보(CartDetail)의 번호를 받아야한다.
    // member entity를 이용해 CartAllDTO를 가져오고,
    // List로 선언된 필드들의 index값이 같은 값들이 하나의 상품이니까,
    // 넘겨받은 장바구니 상품별 정보(CartDetail)의 번호로 List<Long> cdids의 인덱스 번호를 찾고,
    // sizes, colors, paymentSuccesss, itemDTOS, counts이 필드들의 [윗줄에서 찾은 인덱스번호] 번째 값을 가져와 프론트에서 출력한다..
    public CartAllDTO readOne(String mid) { // Principal principal
        // 여기에선 cno가 아닌 member로 장바구니와 장바구니 상품별 정보를 가져와 CartAllDTO로 리턴하게 수정하기
        //String mid = principal.getName();
        System.out.println("mid : " + mid);
        Cart cart = cartRepository.findByMember_Mid(mid);
        // 회원의 장바구니 가져옴
        List<CartDetail> cartDetails = cartDetailRepository.findByCartWherePaymentSuccess(cart);
        // 회원의 장바구니 상세 항목들을 가져옴(결제되지 않은)
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        List<Integer> paymentSuccesss = new ArrayList<>();
        List<Long> cdids = new ArrayList<>();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        // 상세 항목 정보들을 list 타입으로 저장
        cartDetails.forEach(cartDetail -> {
            sizes.add(cartDetail.getSize());
            colors.add(cartDetail.getColor());
            paymentSuccesss.add(cartDetail.getPaymentSuccess());
            cdids.add(cartDetail.getCdid());
            ItemDTO itemDTO = itemService.readOne(cartDetail.getItem().getIno());
            itemDTOS.add(itemDTO);

            Integer count = cartDetail.getCount();
            counts.add(count);
        });

        CartAllDTO cartAllDTO = CartAllDTO.builder()
                    .cno(cart.getCno())
                    .member(cart.getMember().getMid())
                    .sizes(sizes)
                    .colors(colors)
                    .paymentSuccesss(paymentSuccesss)
                    .cdids(cdids)
                    .itemDTOS(itemDTOS)
                    .counts(counts)
                    .build();
        return cartAllDTO;
    }

//    @Transactional
//    public Long orderCartItem(List<CartOrderDTO> cartOrderDtoList, String mid, Long cdid) {
//        // 주문 ID를 생성
//        Long orderId = createNewOrder(mid, cdid);
//
//        // 각 항목에 대한 처리
//        for (CartOrderDTO cartOrder : cartOrderDtoList) {
//            CartItem cartItem = cartRepository.findById(cartOrder.getCartItemId())
//                    .orElseThrow(() -> new IllegalStateException("Invalid cart item ID"));
//
//            if (cartItem.getQuantity() < 1) {
//                throw new IllegalStateException("Out of stock for item ID: " + cartItem.getId());
//            }
//
//            // 재고 감소
//            cartItem.decreaseQuantity(1);
//            cartRepository.save(cartItem);
//
//            // 주문 항목 저장 로직 (생략)
//            saveOrderDetails(orderId, cartItem);
//        }
//
//        return orderId;
//    }
//
//    private Long createNewOrder(String mid, Long cdid) {
//    }


}