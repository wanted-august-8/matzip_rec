package com.matzip.api.matzip_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
@Schema(description = "사용자 위치 업데이트 요청 DTO")
public class LocationUpdateDto {

    @Schema(description = "위도", example = "37.389832")
    @NotNull(message = "위도는 필수 값입니다.")
    @Digits(integer = 8, fraction = 6, message = "위도는 최대 8자리 정수와 6자리 소수로 구성되어야 합니다.")
    private BigDecimal lat;

    @Schema(description = "경도", example = "126.950773")
    @NotNull(message = "경도는 필수 값입니다.")
    @Digits(integer = 9, fraction = 6, message = "경도는 최대 9자리 정수와 6자리 소수로 구성되어야 합니다.")
    private BigDecimal logt;
}
