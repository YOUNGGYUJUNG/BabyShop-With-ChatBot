package com.project.apple.dto.product;


import com.project.apple.domain.Category;
import com.project.apple.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ProductRegistRequest(
        @NotBlank
        String product_name,
        @Min(1)
        int qnt,
        @Min(1)
        int price,
        String image,
        String description,
        Long categoryId
) {
    public Product setProduct(Category category) {
        return Product.builder()
                .productName(this.product_name)
                .qnt(this.qnt)
                .price(this.price)
                .image(this.image)
                .description(this.description)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
