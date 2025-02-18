package com.project.apple.controller;

import com.project.apple.domain.Notice;
import com.project.apple.dto.Notice.InsertNoticeRequestDto;
import com.project.apple.dto.Notice.SelectNoticeListResponseDto;
import com.project.apple.dto.Notice.SelectNoticeResponseDto;
import com.project.apple.dto.Notice.UpdateNoticeDto;
import com.project.apple.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    // 공지사항 목록조회
    @GetMapping
    public ResponseEntity<PagedModel<SelectNoticeListResponseDto>> getNoticeList(Pageable pageable) {
        return ResponseEntity.ok(noticeService.getNoticeList(pageable));
    }

    // 공지사항 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<SelectNoticeResponseDto> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    // 공지사항 등록
    @PostMapping
    public ResponseEntity<Notice> insertNotice(@RequestBody InsertNoticeRequestDto insertNoticeRequestDto) {
        Notice notice = noticeService.insertNotice(insertNoticeRequestDto);
        // 성공 시 200 반환
        return new ResponseEntity<>(notice, HttpStatus.CREATED);
    }

    // 공지사항 검색 (title로 검색)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<SelectNoticeListResponseDto>> getNoticeByTitle(
            @RequestParam String title,
            Pageable pageable) {
        return ResponseEntity.ok(noticeService.selectNotice(title, pageable));
    }

    // 공지사항 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody UpdateNoticeDto updateNoticeDto) {

        Notice notice = noticeService.updateNotice(id, updateNoticeDto);
        return ResponseEntity.ok(notice);
    }
}

