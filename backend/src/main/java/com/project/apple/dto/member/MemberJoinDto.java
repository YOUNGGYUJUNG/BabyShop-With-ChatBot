package com.project.apple.dto.member;

import com.project.apple.validate.ValidatePattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.project.apple.validate.ValidatePattern.*;

@Builder
public record MemberJoinDto(@NotBlank @Email String email,
                         String password,
                        @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 조건: 3~15자, 알파벳, 숫자, 밑줄(_), 하이픈(-)만 허용") String nickname,
                        @NotBlank String address)
{

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public MemberJoinDto {
       if(password.isBlank() || password.length() < 8 || password.length() > 15) {
           throw new IllegalStateException("비밀 번호 조건 : 8 ~15자만 허용");
       }
        password = bCryptPasswordEncoder.encode(password);
    }

}
