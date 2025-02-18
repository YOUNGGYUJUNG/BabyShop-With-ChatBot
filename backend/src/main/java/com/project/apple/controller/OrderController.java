package com.project.apple.controller;


import com.project.apple.annotation.CurrentMember;
import com.project.apple.annotation.MemberOnly;
import com.project.apple.domain.Member;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.order.request.OrderCreateRequestDto;
import com.project.apple.dto.order.request.OrderProductRequestDto;
import com.project.apple.dto.order.response.OrderCartResponseDto;
import com.project.apple.dto.order.response.OrderHistoryResponseDto;
import com.project.apple.dto.order.response.OrderListHistoryResponseDto;
import com.project.apple.dto.order.response.OrderResponseDto;
import com.project.apple.dto.product.CustomPageRequest;
import com.project.apple.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 장바구니 주문
    @PostMapping("/cart/order")
    public List<OrderCartResponseDto> createCartOrder(@RequestBody List<OrderProductRequestDto> requestDto, Member member) {
        return orderService.createCartOrder(requestDto, member);
    }

    // 바로 주문
    @PostMapping("/direct/order")
    @MemberOnly
    public OrderResponseDto createDirect(@RequestBody OrderProductRequestDto requestDto, @CurrentMember Member/*Request*/ member) {
        return orderService.createDirectOrder(requestDto, member);
    }

    // 주문 완료
    @MemberOnly
    @PostMapping("/orders")
    public HttpStatus createOrder(@RequestBody OrderCreateRequestDto requestDto, @CurrentMember MemberRequest member) {
        orderService.createOrder(requestDto, member);
        return HttpStatus.OK;
    }

    // 주문 목록 전체 조회
    @MemberOnly
    @GetMapping("/orders")
    public PagedModel<OrderListHistoryResponseDto> getOrderHistory(@CurrentMember MemberRequest memberId, CustomPageRequest pageRequest) {
        return orderService.orderListHistory(memberId, pageRequest.getPageRequest());
    }

    // 주문 상세 조회
    @GetMapping("/orders/{orderId}")
    public OrderHistoryResponseDto getOrder(@PathVariable Long orderId) {
        return orderService.orederHistory(orderId);
    }


}
