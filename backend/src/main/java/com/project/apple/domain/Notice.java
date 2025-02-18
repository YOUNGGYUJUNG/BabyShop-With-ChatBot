package com.project.apple.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long member_id;

    private String title;
    private String content;

    // LocalDateTime으로 선언
    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @Builder
    public Notice(Long member_id, String title, String content) {
        this.member_id = member_id;
        this.title = title;
        this.content = content;
    }

    // @PrePersist에서 LocalDateTime을 UTC 시간으로 변환 후 Timestamp로 저장
    @PrePersist
    public void prePersist() {
        ZonedDateTime utcCreateAt = LocalDateTime.now().atZone(ZoneOffset.UTC);
        this.create_at = utcCreateAt.toLocalDateTime();  // LocalDateTime으로 저장
        this.update_at = utcCreateAt.toLocalDateTime();  // LocalDateTime으로 저장
    }

    // @PreUpdate에서 update_at만 갱신 (UTC로 변환)
    @PreUpdate
    public void preUpdate() {
        ZonedDateTime utcUpdateAt = LocalDateTime.now().atZone(ZoneOffset.UTC);
        this.update_at = utcUpdateAt.toLocalDateTime();  // LocalDateTime으로 저장
    }
}
