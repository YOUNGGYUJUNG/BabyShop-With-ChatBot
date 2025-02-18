package com.project.apple.service;

import com.project.apple.domain.Faq;
import com.project.apple.domain.Member;
import com.project.apple.dto.Notice.InsertFaqRequestDto;
import com.project.apple.dto.Notice.SelectFaqListResponseDto;
import com.project.apple.dto.Notice.SelectFaqResponseDto;
import com.project.apple.dto.Notice.UpdateFaqDto;
import com.project.apple.repository.FaqRepository;
import com.project.apple.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;


    @Autowired
    private final LoginRepository loginRepository;

    // FAQ 목록조회
    public PagedModel<SelectFaqListResponseDto> getFaqList(Pageable pageable) {
        Page<SelectFaqListResponseDto> result = faqRepository.findAllByOrderByIdDesc(pageable)
                .map(SelectFaqListResponseDto::FromFaqListResponseDto);

        return new PagedModel<>(result);
    }
    // FAQ 검색
    public PagedModel<SelectFaqListResponseDto> selectFaq(String title, Pageable pageable) {
        Page<SelectFaqListResponseDto> result = faqRepository.findByTitleContainingOrderByIdDesc(title, pageable)
                .map(SelectFaqListResponseDto::FromFaqListResponseDto);

        return new PagedModel<>(result);
    }

    // FAQ 상세조회
    public SelectFaqResponseDto getFaq(Long id) {
        // memberId 찾기
        Member member = loginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        // Notice 찾기
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found"));

        SelectFaqResponseDto result = SelectFaqResponseDto.FromFaqResponseDto(faq);
        return result;
    }

    // FAQ 등록
    @Transactional
    public Faq insertFaq(InsertFaqRequestDto insertFaqRequestDto) {
        // memberId 찾기
        Member member = loginRepository.findById(insertFaqRequestDto.getMember_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (insertFaqRequestDto.getTitle() == null || insertFaqRequestDto.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title or Content cannot be null");
        }

        Faq faq = Faq.builder()
                .member_id(member.getId())
                .title(insertFaqRequestDto.getTitle())
                .content(insertFaqRequestDto.getContent())
                .build();
        return faqRepository.save(faq);
    }

    // FAQ 수정
    @Transactional
    public Faq updateFaq(Long id, UpdateFaqDto updateFaqDto) {
        // Faq 조회
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found"));

        // 요청 DTO 검증
        if (updateFaqDto.getTitle() == null || updateFaqDto.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title or Content cannot be null");
        }

        Faq updateFaq = Faq.builder()
                .id(id)
                .member_id(faq.getMember_id()) // 기존 member 유지
                .create_at(faq.getCreate_at()) // 기존 create_at 유지
                .title(updateFaqDto.getTitle())
                .content(updateFaqDto.getContent())
                .build();

        // 저장 후 반환
        return faqRepository.save(updateFaq);
    }
}
