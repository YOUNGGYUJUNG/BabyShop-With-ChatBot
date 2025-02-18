package com.project.apple.validate;

public class ValidatePattern {
    public static final String PASSWORD_REGEXP = "{8,15}$";
    // 비밀번호 조건: 8~15자, 최소 하나의 문자, 숫자, 특수문자 포함
    public static final String NICKNAME_REGEXP = "^[a-zA-Z0-9_-]{3,15}$";
// 닉네임 조건: 3~15자, 알파벳, 숫자, 밑줄(_), 하이픈(-)만 허용
}
