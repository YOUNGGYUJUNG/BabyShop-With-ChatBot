package com.project.apple.dto.member;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberJoinDtoTest {
    @Test
    @DisplayName("record 테스트")
    void record() throws Exception {
        // given
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        MemberJoinDto build = MemberJoinDto.builder()
                .address("sdf")
                .password("123sdkfj$")
                .email("9@3999`")
                .nickname("adf")
                .build();
        //when
        validator.validate(build).forEach(System.out::println);

        //then
//        System.out.println(build);
    }

}