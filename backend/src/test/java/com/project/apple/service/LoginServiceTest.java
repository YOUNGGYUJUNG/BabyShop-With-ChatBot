package com.project.apple.service;

import com.project.apple.domain.Member;
import com.project.apple.repository.LoginRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Test
    void join() {
        Member build = Member.builder().email("6531z@namver")
                .password("123455666")
                .nickname("하쿠낭만알라")
                .build();

    }
}