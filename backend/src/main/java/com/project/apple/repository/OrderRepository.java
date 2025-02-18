package com.project.apple.repository;

import com.project.apple.domain.Member;
import com.project.apple.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMember(Member member);

    /*@Query("select distinct o "+"from Order o " + "join fetch o.orderProducts op " +
            "join fetch op.product " + "where o.member.id = :memberId")*/
    Page<Order> findByMember_Id(@Param("memberId") Long memberId, Pageable pageable);

}
