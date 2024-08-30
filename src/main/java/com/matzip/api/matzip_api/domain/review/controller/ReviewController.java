package com.matzip.api.matzip_api.domain.review.controller;


import com.matzip.api.matzip_api.domain.review.dto.ReviewCreateRequest;
import com.matzip.api.matzip_api.domain.review.service.ReviewService;
import com.matzip.api.matzip_api.global.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CommonResponse> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        CommonResponse response = reviewService.createReview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}