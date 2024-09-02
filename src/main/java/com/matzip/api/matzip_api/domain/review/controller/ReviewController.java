package com.matzip.api.matzip_api.domain.review.controller;


import com.matzip.api.matzip_api.domain.review.dto.ReviewCreateRequest;
import com.matzip.api.matzip_api.domain.review.service.ReviewService;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.auth.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "맛집 평가 셍성", description = "새로운 맛집 평가를 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse> createReview(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody ReviewCreateRequest request) {
        CommonResponse response = reviewService.createReview(userDetails.getUserId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}