package com.project.apple.dto.Notice;

import com.project.apple.domain.Faq;
import com.project.apple.domain.Notice;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class SelectFaqListResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private LocalDateTime create_at;

    public SelectFaqListResponseDto(Long id, String nickname, String title, LocalDateTime create_at) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.create_at = create_at;
    }

    public static SelectFaqListResponseDto FromFaqListResponseDto(Faq faq) {
        return new SelectFaqListResponseDto(
                faq.getId(),
                faq.getMember().getNickname(),
                faq.getTitle(),
                faq.getCreate_at());
    }
}