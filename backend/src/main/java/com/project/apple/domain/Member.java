package com.project.apple.domain;

import com.project.apple.dto.member.MemberRequest;
import com.project.apple.dto.member.MemberUpdateDto;
import com.project.apple.dto.member.MemberJoinDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Table(name = "members",
uniqueConstraints = {
        @UniqueConstraint(name = "UniqueNickAndEmail", columnNames = {"email","nickname"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @Builder.Default
    private boolean isAdmin = false;
    private String address;

    public static Member of(MemberJoinDto memberDto) {
        return Member.builder()
                .email(memberDto.email())
                .password(memberDto.password())
                .nickname(memberDto.nickname())
                .address(memberDto.address())
                .build();
    }

    public Member update(@Valid MemberUpdateDto memberUpdateDto) {
        this.address = memberUpdateDto.address();
        this.nickname = memberUpdateDto.nickname();
        return this;
    }
    
}
