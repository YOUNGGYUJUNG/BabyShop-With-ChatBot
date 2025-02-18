package com.project.apple.common;

import com.project.apple.dto.member.MemberRequest;

import java.util.Optional;

public class JwtContextHolder {
    public static final JwtContextHolder instance = new JwtContextHolder();

    private JwtContextHolder() { }

    private final ThreadLocal<MemberRequest> principal = new ThreadLocal<>();

    public Optional<MemberRequest> getPrincipal() {
        return Optional.ofNullable(principal.get());
    }

    public void setContext(MemberRequest m) {
        principal.set(m);
    }

    public void clearContext() {
        principal.remove();
    }
}
