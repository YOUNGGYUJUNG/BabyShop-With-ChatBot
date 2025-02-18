package com.project.apple.dto.member;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.project.apple.validate.ValidatePattern.NICKNAME_REGEXP;

@Builder
public record MemberUpdateDto(
       @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 길이는 3~15자 입니다.") String nickname,
        String address
) {
}
