package com.project.apple.dto.Notice;

import com.project.apple.domain.Faq;
import com.project.apple.domain.Notice;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class SelectFaqResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime create_at;

    public SelectFaqResponseDto(Long id, String nickname, String title, String content, LocalDateTime create_at) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.create_at = create_at;
    }

    public static SelectFaqResponseDto FromFaqResponseDto(Faq faq) {
        return new SelectFaqResponseDto(
                faq.getId(),
                faq.getMember().getNickname(),
                faq.getTitle(),
                faq.getContent(),
                faq.getCreate_at()
        );
    }
}