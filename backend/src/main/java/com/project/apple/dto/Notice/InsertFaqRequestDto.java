package com.project.apple.dto.Notice;

import com.project.apple.domain.Faq;
import com.project.apple.domain.Member;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InsertFaqRequestDto {
    private Long member_id;
    private String title;
    private String content;

    public InsertFaqRequestDto(Long member_id, String title, String content) {
        this.member_id = member_id;
        this.title = title;
        this.content = content;
    }
}
