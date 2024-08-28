package com.matzip.api.matzip_api.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account", length = 20, unique = true, nullable = false)
    @Length(min = 4, max = 20)
    private String account;

    @Column(name = "password", length = 300, nullable = false)
    @Length(max = 300)
    private String password;

    private double lat;
    private double logt;

    @Column(name = "use_lunch_recommendation", nullable = false)
    private boolean useLunchRecommendation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // 최초 저장 시에만 설정
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();  // 업데이트될 때마다 수정 시간 설정
    }
}
