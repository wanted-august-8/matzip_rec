package com.matzip.api.matzip_api.domain.user.service;

import com.matzip.api.matzip_api.domain.user.dto.LocationUpdateDto;
import com.matzip.api.matzip_api.domain.user.dto.LunchRecommendationDto;
import com.matzip.api.matzip_api.domain.user.dto.UserResponseDto;
import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자의 위치를 업데이트합니다.
     */
    @Transactional
    public UserResponseDto updateUserLocation(Long userId, LocationUpdateDto locationUpdateDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateLocation(locationUpdateDto.getLat(), locationUpdateDto.getLogt());
        return toUserResponseDto(user);
    }

    /**
     * 점심 추천 기능 사용 여부를 업데이트합니다.
     */
    @Transactional
    public UserResponseDto updateLunchRecommendationPreference(Long userId,
        LunchRecommendationDto lunchRecommendationDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateUseLunchRecommendation(lunchRecommendationDto.isUseLunchRecommendation());
        return toUserResponseDto(user);
    }

    /**
     * User 엔티티를 UserResponseDto로 변환하는 메서드
     */
    private UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .account(user.getAccount())
            .lat(user.getLat())
            .logt(user.getLogt())
            .useLunchRecommendation(user.isUseLunchRecommendation())
            .build();
    }

    /**
     * 사용자 정보를 조회하여 패스워드를 제외한 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return toUserResponseDto(user);
    }
}
