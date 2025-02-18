package com.project.apple.dto.Notice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.apple.domain.Faq;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdateFaqDto {
    private String title;
    private String content;

    public UpdateFaqDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
