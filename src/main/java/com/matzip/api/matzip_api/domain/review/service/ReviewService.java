package com.matzip.api.matzip_api.domain.review.service;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import com.matzip.api.matzip_api.domain.review.dto.ReviewCreateRequest;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import com.matzip.api.matzip_api.domain.review.repository.ReviewRepository;
import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestrtRepository restrtRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰를 생성하는 메서드 입니다.
     * 요청을 검증하고, 리뷰를 생성 후 데이터베이스에 저장합니다.
     *
     * @param request 리뷰 요청을 담은 DTO 객체
     * @return CommonResponse<String> 성공 메시지 반환
     */
    @Transactional
    public CommonResponse createReview(Long userId, ReviewCreateRequest request) {
        // 유저와 맛집 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Restrt restrt = restrtRepository.findById(request.getRestrtId())
            .orElseThrow(() -> new CustomException(ErrorCode.RESTRT_NOT_FOUND));

        // Review 생성 및 저장
        Review review = Review.builder()
            .user(user)
            .restrt(restrt)
            .score(request.getScore())
            .content(request.getContent())
            .build();
        reviewRepository.save(review);

        // 맛집의 새로운 평균 평점 계산 및 업데이트
        updateRestaurantRating(restrt);

        return CommonResponse.ok("맛집 평가를 생성하였습니다.", review.getId());
    }

    /**
     * 맛집의 평점 평균을 수정하는 메서드입니다.
     *
     * @param restrt 리뷰의 주체인 맛집
     */
    private void updateRestaurantRating(Restrt restrt) {
        List<Review> reviews = reviewRepository.findByRestrt(restrt);
        double averageScore = reviews.stream()
            .mapToInt(Review::getScore)
            .average()
            .orElse(0.0);
        restrt.setReview(averageScore); // 새로운 평균 평점으로 설정
        restrtRepository.save(restrt); // 맛집 정보 업데이트
    }
}
