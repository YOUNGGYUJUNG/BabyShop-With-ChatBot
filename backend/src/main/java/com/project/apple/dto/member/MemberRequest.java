package com.project.apple.dto.member;

import lombok.Builder;

@Builder
public record MemberRequest (Long id, String nickname){
}
