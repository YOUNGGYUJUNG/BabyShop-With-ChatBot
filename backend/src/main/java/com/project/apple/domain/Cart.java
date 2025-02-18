package com.project.apple.domain;

import jakarta.persistence.*;
import lombok.*;

// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// new Cart() 방지
@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;


    // 수정 Jang
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;


    @Column(name = "qnt", nullable = false)
    private int qnt;

//    @Builder
//    public Cart(Long id, Long memberId, Long productId, Long qnt) {
//        this.id = id;
//        this.memberId = memberId;
//        this.productId = productId;
//        this.qnt = qnt;
//    }
}