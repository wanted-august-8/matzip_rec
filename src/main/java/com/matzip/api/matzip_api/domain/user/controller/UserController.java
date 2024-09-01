package com.matzip.api.matzip_api.domain.user.controller;

import com.matzip.api.matzip_api.domain.user.dto.LocationUpdateDto;
import com.matzip.api.matzip_api.domain.user.dto.LunchRecommendationDto;
import com.matzip.api.matzip_api.domain.user.dto.UserResponseDto;
import com.matzip.api.matzip_api.domain.user.service.UserService;
import com.matzip.api.matzip_api.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "사용자 관련 API")
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 사용자의 위치를 업데이트하는 엔드포인트
     *
     * @param userId            사용자 ID
     * @param locationUpdateDto 위도와 경도를 포함한 업데이트 정보
     * @return 업데이트된 사용자 정보
     */
    @Operation(summary = "사용자 위치 업데이트", description = "사용자의 위치(위도, 경도)를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 위치가 성공적으로 업데이트됨")
    @PatchMapping("/{userId}/location")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateUserLocation(@PathVariable Long userId,
        @Valid @RequestBody LocationUpdateDto locationUpdateDto) {
        UserResponseDto userResponseDto = userService.updateUserLocation(userId, locationUpdateDto);
        return ResponseEntity.ok(CommonResponse.ok("업데이트에 성공했습니다.", userResponseDto));
    }

    /**
     * 사용자의 점심 추천 기능 사용 여부를 업데이트하는 엔드포인트
     *
     * @param userId                 사용자 ID
     * @param lunchRecommendationDto 점심 추천 기능 사용 여부 Dto (true/false)
     * @return 업데이트된 사용자 정보
     */
    @Operation(summary = "점심 추천 기능 사용 여부 업데이트", description = "점심 추천 기능 사용 여부를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "점심 추천 기능 사용 여부가 성공적으로 업데이트됨")
    @PatchMapping("/{userId}/lunchRecommendation")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateLunchRecommendation(
        @PathVariable Long userId,
        @Valid @RequestBody LunchRecommendationDto lunchRecommendationDto
    ) {
        UserResponseDto userResponseDto = userService.updateLunchRecommendationPreference(userId, lunchRecommendationDto);
        return ResponseEntity.ok(CommonResponse.ok("업데이트에 성공했습니다.", userResponseDto));
    }

    /**
     * 패스워드를 제외한 모든 사용자 정보를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 패스워드를 제외한 사용자 정보
     */
    @Operation(summary = "사용자 정보 조회", description = "패스워드를 제외한 모든 사용자 정보를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 조회됨")
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserResponseDto>> getUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUser(userId);
        return ResponseEntity.ok(CommonResponse.ok("사용자 정보 조회에 성공했습니다.", userResponseDto));
    }
}
