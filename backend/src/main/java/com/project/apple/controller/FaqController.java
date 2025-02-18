package com.project.apple.controller;

import com.project.apple.domain.Faq;
import com.project.apple.dto.Notice.InsertFaqRequestDto;
import com.project.apple.dto.Notice.SelectFaqListResponseDto;
import com.project.apple.dto.Notice.SelectFaqResponseDto;
import com.project.apple.dto.Notice.UpdateFaqDto;
import com.project.apple.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {

    private final FaqService faqService;

    // FAQ 목록조회
    @GetMapping
    public ResponseEntity<PagedModel<SelectFaqListResponseDto>> getFaqList(Pageable pageable) {
        return ResponseEntity.ok(faqService.getFaqList(pageable));
    }

    // FAQ 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<SelectFaqResponseDto> getFaq(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFaq(id));
    }

    // FAQ 등록
    @PostMapping
    public ResponseEntity<Faq> insertFaq(@RequestBody InsertFaqRequestDto insertFaqRequestDto) {
        Faq insertFaq = faqService.insertFaq(insertFaqRequestDto);
        return ResponseEntity.ok(insertFaq);
    }

    // FAQ 검색 (title로 검색)
    @GetMapping("/search/{title}")
    public ResponseEntity<PagedModel<SelectFaqListResponseDto>> getNFaqByTitle(
            @PathVariable String title, Pageable pageable) {
        return ResponseEntity.ok(faqService.selectFaq(title, pageable));
    }

    // FAQ 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable Long id, @RequestBody UpdateFaqDto updateFaqDto) {
        try {
            Faq faq = faqService.updateFaq(id, updateFaqDto);
            return ResponseEntity.ok(faq);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
