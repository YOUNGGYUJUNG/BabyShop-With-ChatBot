package com.project.apple.controller;

import com.project.apple.dto.product.*;
import com.project.apple.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "제품 Api", description = "제품 관리 api")
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    @Operation(summary = "제품 추가", description = "제품을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "잘못된 요청입니다")
    })
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRegistRequest productRegist) {
        productService.save(productRegist);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<PagedModel<ProductSelectResponse>> getProduct(CustomPageRequest pageRequest) {
        return ResponseEntity.ok(productService.getAllProducts(pageRequest.getPageRequest()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailSelectResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductPutRequest productPut) {
        productService.setProduct(id, productPut);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
