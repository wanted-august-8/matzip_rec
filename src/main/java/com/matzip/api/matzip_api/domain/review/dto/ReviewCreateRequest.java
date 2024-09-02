package com.matzip.api.matzip_api.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @Schema(description = "맛집 ID", example = "1", required = true)
    @NotNull(message = "맛집 ID는 필수 입력 값입니다.")
    private Long restrtId;

    @Schema(description = "맛집 평점 (1이상 5이하의 자연수)", example = "5", required = true)
    @NotNull(message = "평점은 필수 입력 값입니다.")
    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점 이하여야 합니다.")
    private Integer score;

    @Schema(description = "리뷰 내용", example = "음식이 정말 맛있어요!", required = true)
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
}