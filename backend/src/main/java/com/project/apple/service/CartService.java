package com.project.apple.service;

import com.project.apple.domain.Cart;
import com.project.apple.domain.Member;
import com.project.apple.domain.Product;
import com.project.apple.dto.cart.CartItemDto;
import com.project.apple.dto.cart.CartOrderDto;
import com.project.apple.dto.cart.request.CartItemAddRequest;
import com.project.apple.dto.cart.request.CartItemDeleteRequest;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.repository.CartRepository;
import com.project.apple.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    // 추가 Jang
    private final ProductRepository productRepository;

    private final CartRepository cartRepository;
    // 다른 Repository나 Service가 필요하면 주입

    /**
     * 장바구니에 상품 추가
     * member 처리
     */
    public Long addToCart(CartItemAddRequest cartItemAddRequest, MemberRequest member) {
        // dto 매핑 다시
        // dto -> entity 변환
        // Refactor
        Product product = productRepository.findById(cartItemAddRequest.getProductId()).orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다"));


        Cart cartItem = Cart.builder()
                .memberId(member.id())       // security ?
                .productId(product)     // 변경
                .qnt(cartItemAddRequest.getCartQnt())
                .build();

        Cart savedCart = cartRepository.save(cartItem);

        return savedCart.getId();
    }

    /**
     * 장바구니 목록 조회
     * 재고 수량도 보내줘야함 - front에서 처리
     * 필터 - 최근 몇개월간 조회 (DB에 날짜 없음)
     */
    public List<CartItemDto> getCartList(MemberRequest member) {

        // 회원 ID 등을 세션이나 SecurityContext에서 가져올 수도 있음
        List<Cart> cartItemList = cartRepository.findByMemberId(member.id());// 멤버 아이디 어떻게 받을지

        // Entity -> DTO 매핑
        // Refactor
        return cartItemList.stream()
                .map(cartItem -> CartItemDto.builder()
                        .id(cartItem.getId())
                        // 추가 Jang
                        .productId(cartItem.getProductId().getId())
                        .productName(cartItem.getProductId().getProductName())

                        .quantity(cartItem.getQnt())
                        // 추가 Jang
                        .price(cartItem.getProductId().getPrice() * cartItem.getQnt())
                        .build())
                .collect(Collectors.toList());

    }


    /**
     * 장바구니 수량 변경
     */
    public void updateCartItemCount(CartItemDto cartItemDto) {
        int count = cartItemDto.getQuantity();
        String nowUser = "demoUser";
        // 1) 파라미터 유효성 검증
        if (count <= 0) {
            throw new IllegalArgumentException("최소 1개 이상 담아주세요.");      // 에러처리 통일,  dto로 message 담기?
        }

        // 2) 권한 체크
        //    예: 현재 로그인 사용자의 memberId를 알아낸 뒤, cart.getMemberId()와 같은지 비교
        Long currentMemberId = 1L; // 실제로는 SecurityContext나 세션 등에서 가져옴
        if (!nowUser.equals(currentMemberId)) throw new SecurityException("수정 권한이 없습니다.");        // 수정 권한

        // 3) 유효성, 권한 모두 통과했으므로 업데이트 진행
        // update를 어캐처리 ?
//        cartRepository.incrementQntByCount(count);     // 해당 cartItem id를 파라미터로 주고 수량만 update

    }

    //    -> 날짜도 없고, 주문이 되었으면 장바구니 비워줘야함
    /**
     * 장바구니 삭제
     */
    public void deleteCartItem(CartItemDeleteRequest cartItemDeleteRequest) {

        // List 처리

        // dto -> entity 변환
        // Refactor
        Cart cartItem = Cart.builder()
                .id(cartItemDeleteRequest.getCartItemId())
                .build();

        cartRepository.deleteById(cartItem.getId());

        // 수정 Jang
        // cartRepository.deleteById(cartItemDeleteRequest.getCartItemId());
    }

    /**
     * 장바구니 상품 주문 - 재고 확인된 건수만 주문 가능
     */
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList) {

        // 주문 로직 구현
        // - 선택한 상품만 구입할 수 있는 로직
        // 1) 장바구니 아이템들 가져오기
        // 2) 주문 생성 (주문 엔티티, 주문상품 엔티티 생성)
        // 3) 재고 차감
        // 4) 장바구니에서 삭제 or 상태 변경
        // ...
        // 주문이 끝나고 생성된 orderId 반환
        Long orderId = 999L; // 예시
        return orderId;
    }
}
