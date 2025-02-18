package com.project.apple.util;

import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.member.MemberResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private String secret = "mySuperLongSecretKeyThatIsAtLeast32Bytes";
    private Long exp = 6000000L;
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXIiOnsibmlja25hbWUiOiJoYW5lb2wiLCJpZCI6IjEifSwiaWF0IjoxNzM3MDIwNzU3LCJleHAiOjE3MzcwMjY3NTd9.6lZcudE3KMnmgmt7QG2ReQMHZof19BXqUDQnVBei26U";

    @Test
    void createJwt() {
        System.out.println(secret);
        System.out.println(exp);
        MemberResponse member = MemberResponse.builder()
                .id(1L)
                .nickname("haneol")
                .build();
        String jwt = JwtUtil.createJwt(secret, exp, member);
        System.out.println(jwt);
        token = jwt.substring(7);
    }

    @Test
    void extractMember() {
        MemberRequest memberRequest = JwtUtil.extractMember(secret, token);
        System.out.println(memberRequest);
    }

    @Test
    void validateJwt() {
        System.out.println(JwtUtil.validateJwt(secret, token));
    }
}