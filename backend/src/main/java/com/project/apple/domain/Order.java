package com.project.apple.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@With
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL )
    private List<OrderProduct> orderProducts = new ArrayList<>();

    // 보류
    private String orderNo;

    private int totalPrice;

    private int totalqnt;

    private LocalDateTime createAt;

    @PrePersist
    void at() {
        this.createAt = LocalDateTime.now();
    }


}