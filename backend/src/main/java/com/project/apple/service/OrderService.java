package com.project.apple.service;

import com.project.apple.domain.*;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.order.request.OrderCreateRequestDto;
import com.project.apple.dto.order.request.OrderProductRequestDto;
import com.project.apple.dto.order.response.OrderCartResponseDto;
import com.project.apple.dto.order.response.OrderHistoryResponseDto;
import com.project.apple.dto.order.response.OrderListHistoryResponseDto;
import com.project.apple.dto.order.response.OrderResponseDto;
import com.project.apple.repository.CartRepository;
import com.project.apple.repository.LoginRepository;
import com.project.apple.repository.OrderRepository;
import com.project.apple.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final LoginRepository loginRepository;

    // 상품 바로 주문 -> 결제전
    @Transactional
    public OrderResponseDto createDirectOrder(OrderProductRequestDto request, Member member){

        member = loginRepository.findById(member.getId()).orElseThrow(() -> new IllegalArgumentException("회원 존재안함"));

        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getNickname())
                .memberAddress(member.getAddress())
                .productId(product.getId())
                .productName(product.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice()*request.getQuantity())
                .build();

        return responseDto;
    }

    // 장바구니 -> 결제전
    @Transactional
    public List<OrderCartResponseDto> createCartOrder(List<OrderProductRequestDto> requestDto, Member member) {

        // 회원의 전체 장바구니 조회(JPQL)
        List<Cart> cartList = cartRepository.findByMemberWithProducts();

        // 주문 요청된 상품 ID 목록 생성
        List<Long> requestedProductIds = requestDto.stream()
                .map(OrderProductRequestDto::getProductId)
                .toList();

        // 주문 요청에 해당하는 장바구니 항목 필터링 (이제 cart.getProduct()를 사용)
        List<Cart> orderCartItems = cartList.stream()
                .filter(cart -> requestedProductIds.contains(cart.getProductId().getId()))
                .toList();

        // 각 장바구니 항목을 OrderCartResponseDto로 변환
        List<OrderCartResponseDto> cartItemDtoList = new ArrayList<>();
        for (Cart cart : orderCartItems) {
            // 이미 Cart에 연결된 Product가 있으므로 추가 조회 없이 사용 가능
            Product product = cart.getProductId();
            OrderCartResponseDto itemDto = OrderCartResponseDto.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .quantity(cart.getQnt())
                    .build();
            cartItemDtoList.add(itemDto);
        }
        return cartItemDtoList;
    }

    // 주문 등록
    @Transactional
    public void createOrder(OrderCreateRequestDto request, MemberRequest member) {

        int totalPrice = 0;
        int totalQuantity = 0;

        // 빈 Order 객체를 생성합니다.
        Order order = Order.builder().build();
        order = orderRepository.save(order);

        // 주문 상품을 담을 리스트
        List<OrderProduct> orderProducts = new ArrayList<>();

        // 각 주문 상품 처리
        for (OrderProductRequestDto productRequest : request.getProducts()) {

            // 재고 감소 및 예외 처리
            sellProduct(productRequest);

            // 상품 조회 (없으면 예외 발생)
            Product product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

            // 주문 상품 생성 시, 초기 주문참조
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(productRequest.getQuantity())
                    .price(productRequest.getPrice())
                    .build();

            orderProducts.add(orderProduct);

            totalPrice += orderProduct.getPrice() * orderProduct.getQuantity();
            totalQuantity += orderProduct.getQuantity();
        }
        // 1.22 생성

        Member orderMember = loginRepository.findByOne(member.id());

        // 초기 Order 객체를 복사후 설정
        Order completeOrder = order.withOrderProducts(orderProducts)
                .withMember(orderMember)
                .withTotalqnt(totalQuantity)
                .withTotalPrice(totalPrice)
                .withCreateAt(LocalDateTime.now());

        orderRepository.save(completeOrder);
    }

    // 주문 상세 조회
    public OrderHistoryResponseDto orederHistory(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        return OrderHistoryResponseDto.fromOrder(order);

    }

    // 주문 전체 조회
    // 매개변수 멤버로 바꿔야함
    public PagedModel<OrderListHistoryResponseDto> orderListHistory(MemberRequest member, Pageable pageable) {
        Page<OrderListHistoryResponseDto> result = orderRepository.findByMember_Id(member.id(), pageable)
                .map(OrderListHistoryResponseDto::fromOrder);
        return new PagedModel<>(result);
    }

    // 상품 재고 감소 메서드
    private void sellProduct(OrderProductRequestDto request){
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalStateException("상품이 없습니다."));

        if(product.getQnt() < request.getQuantity()){
            throw new IllegalStateException("재고 부족가 부족합니다.");
        }

        int quantity = product.getQnt() - request.getQuantity();

        product.setQnt(quantity);
    }
}
