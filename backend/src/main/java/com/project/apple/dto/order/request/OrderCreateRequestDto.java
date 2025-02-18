package com.project.apple.dto.order.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequestDto {

    // 주문 생성시 뭐가 필요할까?
    // 주문 날짜, 주문 상품
    private LocalDateTime orderDate;

    // 주문할 상품을 담을 리스트
    private List<OrderProductRequestDto> products;

}
