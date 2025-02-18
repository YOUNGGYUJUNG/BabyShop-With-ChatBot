package com.project.apple.dto.member;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public record MemberResponse(Long id, String nickname) {

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("nickname", nickname);
        return map;
    }

    public static MemberResponse fromMap(Map<String, Object> map) {
        Long id = Long.valueOf((String)map.get("id"));
        String nickname = (String) map.get("nickname");

        return MemberResponse.builder()
                .id(id)
                .nickname(nickname).build();
    }
}
