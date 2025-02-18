package com.project.apple.repository;

import com.project.apple.domain.Cart;
import com.project.apple.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMemberId(Long memberId);

    // JPA 1차 캐시에 알려줌
    @Modifying
    @Query("UPDATE Cart c SET c.qnt = c.qnt + :count WHERE c.memberId = :memberId AND c.productId = :productId")
    int incrementQntByCount(@Param("memberId") String memberId,
                            @Param("productId") String productId,
                            @Param("count") int count);

    /*@Query("SELECT c FROM Cart c JOIN FETCH c.productId WHERE c.memberId = :member")
    List<Cart> findByMemberWithProducts(@Param("member") Member member);*/

    @Query("SELECT c FROM Cart c JOIN FETCH c.productId WHERE c.memberId = 1")
    List<Cart> findByMemberWithProducts();
}