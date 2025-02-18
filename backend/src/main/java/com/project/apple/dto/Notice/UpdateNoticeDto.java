package com.project.apple.dto.Notice;

import com.project.apple.domain.Notice;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdateNoticeDto {
    private String title;
    private String content;

    public UpdateNoticeDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
