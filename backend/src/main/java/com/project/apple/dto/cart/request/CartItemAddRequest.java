package com.project.apple.dto.cart.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@Builder
@NoArgsConstructor
public class CartItemAddRequest {
    private Long productId;
    private int cartQnt;

    // @Builder 처리
    @Builder
    public CartItemAddRequest(Long productId, int cartQnt) {
        this.productId = productId;
        this.cartQnt = cartQnt;
    }

}
