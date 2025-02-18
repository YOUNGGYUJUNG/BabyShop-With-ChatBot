package com.project.apple.auth.jwt;

import com.project.apple.domain.Member;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.member.MemberResponse;
import com.project.apple.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService{
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiredMs}")
    private Long EXPIRATION;

    public String generateToken(MemberResponse member){
        return JwtUtil.createJwt(secretKey,EXPIRATION,member);
    }
    public boolean validateToken(String token){
        return JwtUtil.validateJwt(secretKey, token);
    }
    public MemberRequest getMember(String token){
        return JwtUtil.extractMember(secretKey,token);
    }
}

