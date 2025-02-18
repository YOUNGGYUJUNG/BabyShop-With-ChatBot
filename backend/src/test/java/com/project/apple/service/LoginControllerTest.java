package com.project.apple.service;

import com.project.apple.controller.LoginController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private LoginController loginController;


    @Test
    @DisplayName("인터셉터 동작 확인")
    void login() throws Exception {
        // given

        //when

        //then

    }
}
