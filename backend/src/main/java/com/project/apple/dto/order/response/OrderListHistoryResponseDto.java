package com.project.apple.dto.order.response;

import com.project.apple.domain.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderListHistoryResponseDto {

    private Long id;

    private List<OrderProductResponseDto> orderProducts;


    private LocalDateTime orderDate;

    // 엔티티 -> 디티오
    public static OrderListHistoryResponseDto fromOrder(Order order) {

        List<OrderProductResponseDto> orderProductDtos = order.getOrderProducts()
                .stream()
                .map(OrderProductResponseDto::fromEntity)
                .toList();

        return OrderListHistoryResponseDto.builder()
                .id(order.getId())
                .orderDate(order.getCreateAt())
                .orderProducts(orderProductDtos)
                .build();
    }
}
