package com.project.apple.repository;

import com.project.apple.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 삭제_테스트() {
        productRepository.deleteById(2L);

    }

    @Test
    public void 삭제_예외_발생() {

        Optional<Product> result = productRepository.findById(2L);

        assertSame(false, result.isPresent());
    }

    @Test
    public void 삭제_예외_수정() {
        productRepository.findById(2L).orElseThrow();
        //no Such Elemental
    }

}