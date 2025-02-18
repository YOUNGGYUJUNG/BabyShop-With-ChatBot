package com.project.apple.dto.order.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductRequestDto {

    // 상품에 관한 데이터를 전달 (아이디, 이름, 수량, 가격)
    private Long productId;

    // 주문 수량
    private int quantity;

    private int price;

}