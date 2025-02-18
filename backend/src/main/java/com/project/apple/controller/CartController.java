package com.project.apple.controller;

import com.project.apple.annotation.CurrentMember;
import com.project.apple.annotation.MemberOnly;
import com.project.apple.domain.Member;
import com.project.apple.dto.cart.CartItemDto;
import com.project.apple.dto.cart.CartOrderDto;
import com.project.apple.dto.cart.request.CartItemAddRequest;
import com.project.apple.dto.cart.request.CartItemDeleteRequest;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 상품 중복 처리: 동일 상품을 여러 번 장바구니에 추가할 경우 수량 증가 처리.
// 자동 만료 처리: 일정 기간 동안 구매하지 않은 장바구니 항목을 자동으로 삭제.
// 장바구니 복원: 사용자가 로그인하면 이전에 저장된 장바구니 항목 복원.

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 상품 추가
     */
    @MemberOnly
    @PostMapping(value = "/cart")
    public ResponseEntity<Void> addCartItem(@RequestBody @Valid CartItemAddRequest cartItemAddRequest, @CurrentMember MemberRequest member) {
        // 서비스 호출로 새로운 장바구니 항목 생성
        Long cartItemId = cartService.addToCart(cartItemAddRequest, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 장바구니 조회
     */
    @MemberOnly
    @GetMapping(value = "/cart")
    public ResponseEntity<List<CartItemDto>> getCartList(@CurrentMember MemberRequest member) {
        List<CartItemDto> cartItems = cartService.getCartList(member);
        return ResponseEntity.ok(cartItems);


    }

    /**
     * 장바구니 수량 변경
     * 사용자 처리
     */
    @PatchMapping(value = "/cart")
    public ResponseEntity<String> updateCartItem(@RequestBody @Valid CartItemDto cartItemDto) {

        cartService.updateCartItemCount(cartItemDto);
        return ResponseEntity.ok("Cart item updated successfully");
    }

    /**
     * 장바구니 상품 삭제
     * 상품 개별 삭제, 전체 삭제
     */
    @DeleteMapping(value = "/cart")
    public ResponseEntity<Void> deleteCartItem(@RequestBody @Valid CartItemDeleteRequest cartItemDeleteRequest) {

        cartService.deleteCartItem(cartItemDeleteRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 주문
     * Refactor
     */

    @PostMapping(value = "/cart/orders")
    public ResponseEntity<?> orderCartItem(@RequestBody CartOrderDto cartOrderDto) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        Long orderId = cartService.orderCartItem(cartOrderDtoList);
        return ResponseEntity.ok(orderId);
    }
}