package com.matzip.api.matzip_api.domain.user.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String account;
    private BigDecimal lat;
    private BigDecimal logt;
    private boolean useLunchRecommendation;
}
