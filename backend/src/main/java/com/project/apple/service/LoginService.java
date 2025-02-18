package com.project.apple.service;

import com.project.apple.domain.Member;
import com.project.apple.dto.member.*;
import com.project.apple.repository.LoginRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;


    public void join(MemberJoinDto memberDto) {
        valildateDuplicateMember(memberDto);
        Member member = Member.of(memberDto);
        try{
            loginRepository.save(member);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("유니크에서 걸렸다.");
        }
    }

    private void valildateDuplicateMember(MemberJoinDto memberDto) {
        if (emailCheck(memberDto.email())) {
            throw new IllegalStateException("이메일이 중복입니다.");
        }
        if (nicknameCheck(memberDto.nickname())) {
            throw new IllegalStateException("닉네임이 중복입니다.");
        }
    }
    public boolean emailCheck(String email) {
        return loginRepository.existsByNickname(email);
    }
    public boolean nicknameCheck(String nickname) {
        return loginRepository.existsByNickname(nickname);
    }

    public void update(@Valid MemberUpdateDto memberUpdateDto, Long memberId) {
        Member member = loginRepository.findById(memberId).map(m -> m.update(memberUpdateDto)).orElseThrow(
                () -> new IllegalStateException("회원이 없습니다.")
        );
        loginRepository.save(member);
    }

    public MemberResponse login(@Valid MemberLoginDto memberLoginDto) {
        Member member = loginRepository.findByEmail(memberLoginDto.email()).orElseThrow(
                () -> new IllegalStateException("비밀번호 또는 아이디가 일치하지 않습니다.")
        );
        // 아이디 확인
        // 비밀 번호 확인
        if (!passwordEncoder.matches(memberLoginDto.password(), member.getPassword())) {
            throw new IllegalStateException("비밀번호 또는 아이디가 일치하지 않습니다.");
        }
        return MemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
