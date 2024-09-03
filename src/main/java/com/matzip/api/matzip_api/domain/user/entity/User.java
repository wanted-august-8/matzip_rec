package com.matzip.api.matzip_api.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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

    @Column(precision = 8, scale = 6)  // 위도: 소수점 이하 6자리까지 정밀도
    private BigDecimal lat;

    @Column(precision = 9, scale = 6)  // 경도: 소수점 이하 6자리까지 정밀도
    private BigDecimal logt;

    @Column(name = "use_lunch_recommendation", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean useLunchRecommendation;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    public void updateLocation(BigDecimal lat, BigDecimal logt) {
        this.lat = lat;
        this.logt = logt;
    }

    public void updateUseLunchRecommendation(boolean useLunchRecommendation) {
        this.useLunchRecommendation = useLunchRecommendation;
    }
}
