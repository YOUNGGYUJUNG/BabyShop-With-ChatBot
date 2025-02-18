package com.project.apple.dto.order.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponseDto {

    // 회원 정보
    private Long memberId;

    private String memberName;

    private String memberAddress;

    // 상품 정보
    private Long productId;

    private String productName;

    private int quantity;

    private int price;
}
