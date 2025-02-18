package com.project.apple.dto.cart.request;

import lombok.Data;

@Data
public class CartItemDeleteRequest {
    private Long cartItemId;
    // private String message;
    // 삭제 확인 메시지 용도
}
