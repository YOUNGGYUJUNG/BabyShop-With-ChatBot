package com.project.apple.controller;

import com.project.apple.annotation.CurrentMember;
import com.project.apple.annotation.MemberOnly;
import com.project.apple.auth.jwt.JwtService;
import com.project.apple.dto.member.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.project.apple.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class LoginController {
    private final LoginService loginService;
    private final JwtService jwtService;

    /**
     * @param memberJoinDto
     * @return
     * @Description 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid MemberJoinDto memberJoinDto) {
        loginService.join(memberJoinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    /**
     * @param memberUpdateDto
     * @return
     * @Despciption 회원 수정
     */
    @PutMapping
    @MemberOnly
    public ResponseEntity<String> update(@RequestBody @Valid MemberUpdateDto memberUpdateDto, @CurrentMember MemberRequest memberRequest) {
        loginService.update(memberUpdateDto, memberRequest.id());
        return ResponseEntity.ok("회원 정보를 수정했습니다.");
    }

    /**
     * @param memberLoginDto
     * @return
     * @Descrption 로그인 기능
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberLoginDto memberLoginDto )
    {
        MemberResponse memberdto = loginService.login(memberLoginDto);
        // jwt 토큰 정보 제공
        String token = jwtService.generateToken(memberdto);
        // 헤더에 jwt send
        return ResponseEntity.ok().headers(sendToken(token)).body("로그인 하였습니다.");
    }

    private HttpHeaders sendToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }

    /**
     * @param map
     * @return
     * @Description 닉네임 체크
     */
    @PostMapping("/nickname")
    public ResponseEntity<Boolean> nicknameCheck(@RequestBody Map<String, String> map) {
        String nickname = map.get("nickname");
        return ResponseEntity.ok(!loginService.nicknameCheck(nickname));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // jwt 처리 해줘야지

        return ResponseEntity.ok("로그아웃 하셨습니다.");
    }
}
