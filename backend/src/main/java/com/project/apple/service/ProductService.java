package com.project.apple.service;

import com.project.apple.domain.Category;
import com.project.apple.domain.Product;
import com.project.apple.dto.product.ProductDetailSelectResponse;
import com.project.apple.dto.product.ProductPutRequest;
import com.project.apple.dto.product.ProductRegistRequest;
import com.project.apple.dto.product.ProductSelectResponse;
import com.project.apple.repository.CategoryRepository;
import com.project.apple.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public void save(ProductRegistRequest productRegistRequest) {
        productRepository.save(productRegistRequest.setProduct(
                categoryRepository.findById(productRegistRequest.categoryId()).orElseThrow()
        ));
    }

    public PagedModel<ProductSelectResponse> getAllProducts(Pageable pageable) {
        Page<ProductSelectResponse> results = productRepository.findAll(pageable)
                .map(ProductSelectResponse::from);

        return new PagedModel<>(results);
    }

    public ProductDetailSelectResponse getProductById(Long id) {
        Product results = productRepository.findById(id).orElseThrow();
        return ProductDetailSelectResponse.from(results);
    }

    @Transactional
    public void setProduct(Long id, ProductPutRequest productPutRequest) {
        Product product = productRepository.findById(id).orElseThrow();
        Category category = categoryRepository.findById(productPutRequest.categoryId()).orElseThrow();
        productPutRequest.setProduct(product, category); //dirtyChecking
    }

    public void deleteProduct(Long id) {
        productRepository.delete(
                productRepository.findById(id).orElseThrow()
        );
    }

}
