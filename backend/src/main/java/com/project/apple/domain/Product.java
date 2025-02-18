package com.project.apple.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name="product_name")
    private String productName;

    private int qnt;

    private boolean isSold;

    private int price;

    private LocalDateTime createdAt;

    private String image;

    private String description;

}
