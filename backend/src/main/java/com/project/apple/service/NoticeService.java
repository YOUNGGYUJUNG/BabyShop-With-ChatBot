package com.project.apple.service;

import com.project.apple.domain.Member;
import com.project.apple.domain.Notice;
import com.project.apple.dto.Notice.InsertNoticeRequestDto;
import com.project.apple.dto.Notice.SelectNoticeListResponseDto;
import com.project.apple.dto.Notice.SelectNoticeResponseDto;
import com.project.apple.dto.Notice.UpdateNoticeDto;
import com.project.apple.repository.LoginRepository;
import com.project.apple.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    private final LoginRepository loginRepository;


    // 공지사항 목록조회
    public PagedModel<SelectNoticeListResponseDto> getNoticeList(Pageable pageable) {
        Page<SelectNoticeListResponseDto> result = noticeRepository.findAllByOrderByIdDesc(pageable)
                .map(SelectNoticeListResponseDto::FromNoticeListResponseDto);

        return new PagedModel<>(result);
    }

    // 공지사항 상세조회
    public SelectNoticeResponseDto getNotice(Long id) {
        // memberId 찾기
        Member member = loginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        // Notice 찾기
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found"));

        SelectNoticeResponseDto result = SelectNoticeResponseDto.FromNoticeResponseDto(notice);
        return result;
    }

    // 공지사항 검색
    public PagedModel<SelectNoticeListResponseDto> selectNotice(String title, Pageable pageable) {
        Page<SelectNoticeListResponseDto> result = noticeRepository.findByTitleContainingOrderByIdDesc(title, pageable)
                .map(SelectNoticeListResponseDto::FromNoticeListResponseDto);
        return new PagedModel<>(result);
    }

    // 공지사항 등록
    @Transactional
    public Notice insertNotice(InsertNoticeRequestDto insertNoticeRequestDto) {
        // memberId 찾기
        Member member = loginRepository.findById(insertNoticeRequestDto.getMember_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (insertNoticeRequestDto.getTitle() == null || insertNoticeRequestDto.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title or Content cannot be null");
        }
        Notice notice = Notice.builder()
                .member_id(insertNoticeRequestDto.getMember_id())
                .title(insertNoticeRequestDto.getTitle())
                .content(insertNoticeRequestDto.getContent())
                .build();
        return noticeRepository.save(notice);
    }

    // 공지사항 수정
    @Transactional
    public Notice updateNotice(Long noticeId, UpdateNoticeDto updateNoticeDto) {
        // Notice 조회
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found"));

        // 요청 DTO 검증
        if (updateNoticeDto.getTitle() == null || updateNoticeDto.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title or Content cannot be null");
        }

        Notice updateNotice = Notice.builder()
                .member_id(notice.getMember_id())
                .id(noticeId)
                .create_at(notice.getCreate_at())
                .title(updateNoticeDto.getTitle())
                .content(updateNoticeDto.getContent())
                .build();
        // 저장 후 반환
        return noticeRepository.save(updateNotice);
    }
}
