package com.project.apple.dto.order.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCartResponseDto {

    private Long productId;

    private String productName;

    private int price;

    private int quantity;

    private int totalPrice;

}
