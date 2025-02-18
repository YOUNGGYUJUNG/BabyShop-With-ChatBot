package com.project.apple.common;

import com.project.apple.annotation.CurrentMember;
import com.project.apple.auth.jwt.JwtService;
import com.project.apple.dto.member.MemberRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtContextHolder contextHolder = JwtContextHolder.instance;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class) && Objects.equals(parameter.getParameter().getType(), MemberRequest.class);
    }

    @Override
    public MemberRequest resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                         WebDataBinderFactory binderFactory) throws Exception {

        // jwt 값에서 MemberRequest로 반환하기
        // jwtContextHolder에서 값만 가져오기
        // 만약 값이 없으면 예외 던지기
        return contextHolder.getPrincipal().orElseThrow(
                () -> new IllegalStateException("인증이 안되어 있습니다.")
        );
    }
    // 검증
}
