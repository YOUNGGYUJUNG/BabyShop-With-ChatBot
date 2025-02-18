package com.project.apple.repository;

import com.project.apple.domain.Faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    // FAQ 목록조회
    Page<Faq> findAllByOrderByIdDesc(Pageable pageable);

    // 공지사항 검색)
    Page<Faq> findByTitleContainingOrderByIdDesc(String title, Pageable pageable);
}
