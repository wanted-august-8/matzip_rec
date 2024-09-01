package com.matzip.api.matzip_api.domain.restrt.controller;


import static com.matzip.api.matzip_api.global.error.ErrorCode.SORT_PARAMETER_INVALID;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorResponse;
import com.matzip.api.matzip_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restrt")
@RequiredArgsConstructor
public class RestrtController {

    private final RestrtService restrtService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RestrtDetailResponseDto>> getRestrtDetail(
        @PathVariable Long id) {
        CommonResponse<RestrtDetailResponseDto> response = restrtService.getRestrtDetail(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse<RestrtDetailResponseDto>> getRestrtListByLatAndLone(
        @RequestParam("lat") String lat, @RequestParam("lon") String lon,
        @RequestParam("range") double range, @RequestParam(value = "sort", required = false, defaultValue = "distance") String sort
        ) {

        if (!sort.equals("distance") && !sort.equals("rating")) throw new CustomException(SORT_PARAMETER_INVALID);

        List<RestrtListByResponseDto> dtos = restrtService.getRestrtListByLatAndLon(lat, lon, range, sort);

        String message;
        if (dtos.isEmpty()) message = "조회 결과가 없습니다.";
        else message = String.format("%d건 조회 되었습니다.", dtos.size());

        return new ResponseEntity(CommonResponse.ok(message, dtos), HttpStatus.OK);
    }

}