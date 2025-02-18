package com.project.apple.dto.cart;

import com.project.apple.domain.Cart;
import com.project.apple.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Data
public class CartItemDto {
  private Long id;                  // 장바구니 아이템 PK
  private Long productId;           // 상품 ID
  private String productName;       // 상품명

  // 가격 Long -> int Jang
  private int price;                // 상품 가격
  private int quantity;             // 수량

  @Builder
  public CartItemDto(Long id, Long productId, String productName, int price, int quantity) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
  }

}