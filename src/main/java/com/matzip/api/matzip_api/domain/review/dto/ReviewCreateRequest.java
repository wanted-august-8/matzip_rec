package com.matzip.api.matzip_api.domain.review.dto;

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

    @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
    private Long userId;

    @NotNull(message = "맛집 ID는 필수 입력 값입니다.")
    private Long restrtId;

    @NotNull(message = "평점은 필수 입력 값입니다.")
    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점 이하여야 합니다.")
    private Integer score;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
}