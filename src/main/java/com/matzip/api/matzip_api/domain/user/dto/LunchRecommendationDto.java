package com.matzip.api.matzip_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "점심 추천 기능 사용 여부 업데이트 요청 DTO")
public class LunchRecommendationDto {

    @Schema(description = "점심 추천 기능 사용 여부", example = "true")
    @NotNull(message = "점심 추천 기능 사용 여부는 필수 값입니다.")
    private boolean useLunchRecommendation;
}
