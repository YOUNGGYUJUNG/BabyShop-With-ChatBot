package com.project.apple.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.apple.annotation.CurrentMember;
import com.project.apple.annotation.MemberOnly;
import com.project.apple.auth.jwt.JwtService;
import com.project.apple.dto.member.MemberRequest;
import com.project.apple.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtCheckInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private JwtContextHolder contextHolder = JwtContextHolder.instance;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        boolean hasAnnotation = checkAnnotation(handler, MemberOnly.class);

        if(!hasAnnotation){
            return true;
        }
        log.info("jwt 검증");

        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if(jwtService.validateToken(token)){
                // current Member 만들어서 JwtContextHolder에 추가하는 작업을 구현
                MemberRequest member = jwtService.getMember(token);
                contextHolder.setContext(member);
                return true;
            }
        }
        // 없거나 유효하지 않으면 401 에러를 던진다.
        sendErrorResponse(response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // JwtContextHolder 비우기
        contextHolder.clearContext();
    }

    private void sendErrorResponse(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> responseBody= new HashMap<>();

        responseBody.put("message","로그인 해주세요");

        try{
            String body = objectMapper.writeValueAsString(responseBody);
            response.getWriter().write(body);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"message\": \"직렬화 에러\"}");
            throw new Exception();
        }

    }

    private boolean checkAnnotation(Object handler, Class<MemberOnly> authClass) {
        if (handler instanceof HandlerMethod handlerMethod) {
            //Auth 어노테이션이 있는 경우
            if (null != handlerMethod.getMethodAnnotation(authClass) || null != handlerMethod.getBeanType().getAnnotation(authClass)) {
                return true;
            }
        }
           //annotation이 없는 경우
        return false;
    }
}
