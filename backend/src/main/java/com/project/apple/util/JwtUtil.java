package com.project.apple.util;

import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.member.MemberResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private final static String BEARER = "Bearer ";

    // jwt 생성
    public static String createJwt(String secretKey, Long expire, MemberResponse member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("member", member.toMap());
        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(getKey(secretKey))
                .compact();
        return BEARER + token;
    }

    public static MemberRequest extractMember(String secretKey, String token) {
        Claims claims = getClaims(getKey(secretKey), token);
        Map<String, Object> map = claims.get("member", Map.class);

        MemberResponse member = MemberResponse.fromMap(map);
        return MemberRequest.builder().id(member.id()).nickname(member.nickname()).build();

    }

    // jwt 검증
    public static boolean validateJwt(String secretKey, String token) {
        try {
            Jwts.parser().verifyWith(getKey(secretKey)).build().parseSignedClaims(token);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token");
        }
        return true;
    }

    // jwt 값 가져오기
    private static Claims getClaims(SecretKey key, String token) {
        return Jwts.parser().verifyWith(key).build().parseClaimsJws(token).getPayload();
    }

    private static SecretKey getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
