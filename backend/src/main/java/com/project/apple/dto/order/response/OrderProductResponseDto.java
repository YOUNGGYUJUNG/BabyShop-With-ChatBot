package com.project.apple.dto.order.response;

import com.project.apple.domain.OrderProduct;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderProductResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;

    public static OrderProductResponseDto fromEntity(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .id(orderProduct.getId())
                .productId(orderProduct.getProduct().getId())
                .productName(orderProduct.getProduct().getProductName())
                .price(orderProduct.getPrice())
                .quantity(orderProduct.getQuantity())
                .build();
    }
}