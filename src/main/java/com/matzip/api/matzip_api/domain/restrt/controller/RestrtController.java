package com.matzip.api.matzip_api.domain.restrt.controller;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import com.matzip.api.matzip_api.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restrt")
@RequiredArgsConstructor
public class RestrtController {
    private final RestrtService restrtService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData() throws Exception {
        restrtService.fetchData();
        return ResponseEntity.ok("fetch data successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RestrtDetailResponseDto>> getRestrtDetail(@PathVariable Long id) {
        CommonResponse<RestrtDetailResponseDto> response = restrtService.getRestrtDetail(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}