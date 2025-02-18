package com.project.apple.dto.product;

import com.project.apple.domain.Product;

public record ProductDetailSelectResponse(
        // 1.21 추가
        Long id,

        String productName,
        int qnt,
        boolean isSold,
        int price,
        String image,
        String categoryName,
        String description
) {
    public static ProductDetailSelectResponse from(Product product) {
        return new ProductDetailSelectResponse(
                // 1.21 추가
                product.getId(),

                product.getProductName(),
                product.getQnt(),
                product.isSold(),
                product.getPrice(),
                product.getImage(),
                (product.getCategory().getParentCategory().getCategoryName() + " - " + product.getCategory().getCategoryName()),
                product.getDescription()
        );
    }
}
