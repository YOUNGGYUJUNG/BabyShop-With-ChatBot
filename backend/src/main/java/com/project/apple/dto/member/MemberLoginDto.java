package com.project.apple.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import static com.project.apple.validate.ValidatePattern.PASSWORD_REGEXP;

public record MemberLoginDto(
        @Email String email,
        String password
) { }
