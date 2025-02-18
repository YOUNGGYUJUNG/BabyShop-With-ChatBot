package com.project.apple.repository;

import com.project.apple.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 공지사항 목록조회
    Page<Notice> findAllByOrderByIdDesc(Pageable pageable);

    // 공지사항 검색)
    Page<Notice> findByTitleContainingOrderByIdDesc(String title, Pageable pageable);
}