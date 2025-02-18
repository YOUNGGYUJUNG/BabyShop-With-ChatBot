package com.project.apple.dto.product;

import com.project.apple.domain.Category;
import com.project.apple.domain.Product;

public record ProductPutRequest(
        String productName,
        int qnt,
        boolean isSold,
        int price,
        String image,
        String description,
        Long categoryId
) {
    public void setProduct(Product product, Category category) {
        product.setProductName(this.productName);
        product.setQnt(this.qnt);
        product.setSold(this.isSold);
        product.setPrice(this.price);
        product.setImage(this.image);
        product.setDescription(this.description);
        product.setCategory(category);
    }
}
