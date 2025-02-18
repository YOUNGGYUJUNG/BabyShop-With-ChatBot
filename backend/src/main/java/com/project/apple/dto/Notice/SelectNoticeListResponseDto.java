package com.project.apple.dto.Notice;

import com.project.apple.domain.Notice;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class SelectNoticeListResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private LocalDateTime create_at;

    public SelectNoticeListResponseDto(Long id, String nickname, String title, LocalDateTime create_at) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.create_at = create_at;
    }

    public static SelectNoticeListResponseDto FromNoticeListResponseDto(Notice notice) {
        return new SelectNoticeListResponseDto(
                notice.getId(),
                notice.getMember().getNickname(),
                notice.getTitle(),
                notice.getCreate_at());
    }
}
