package com.project.apple.repository;

import com.project.apple.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    Optional<Member> findByEmail(String email);

    default Member findByOne(Long id) {
        return findById(id).orElseThrow(
                () -> new IllegalStateException("회원이 없습니다.")
        );
    }
}
