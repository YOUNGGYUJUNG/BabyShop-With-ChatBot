package com.project.apple.dto.Notice;

import com.project.apple.domain.Notice;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class SelectNoticeResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime create_at;

    public SelectNoticeResponseDto(Long id, String nickname, String title, String content, LocalDateTime createAt) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.create_at = createAt;
    }

    public static SelectNoticeResponseDto FromNoticeResponseDto(Notice notice) {
        return new SelectNoticeResponseDto(
                notice.getId(),
                notice.getMember().getNickname(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreate_at()
        );
    }
}
