package com.matzip.api.matzip_api.domain.restrt.controller;

import static com.matzip.api.matzip_api.global.error.ErrorCode.SORT_PARAMETER_INVALID;
import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<CommonResponse<RestrtDetailResponseDto>> getRestrtDetail(
        @PathVariable Long id) {
        CommonResponse<RestrtDetailResponseDto> response = restrtService.getRestrtDetail(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "검색할 위도,경도로부터 범위 내에 존재하는 맛집 목록", description = "검색할 위도,경도로 부터 범위 내에 존재하는 맛집 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<CommonResponse<RestrtDetailResponseDto>> getRestrtListByLatAndLone(
        @Parameter(description = "위도", example = "37.487962",required = true)
        @RequestParam("lat") String lat,
        @Parameter(description = "경도", example = "127.039882",required = true)
        @RequestParam("lon") String lon,
        @Parameter(description = "범위", example = "40.0",required = true)
        @RequestParam("range") double range,
        @Parameter(description = "정렬: 거리순(distance) or 평점순(rating)", example = "distance")
        @RequestParam(value = "sort", required = false, defaultValue = "distance") String sort
        ) {

        if (!sort.equals("distance") && !sort.equals("rating")) throw new CustomException(SORT_PARAMETER_INVALID);

        List<RestrtListByResponseDto> dtos = restrtService.getRestrtListByLatAndLon(lat, lon, range, sort);

        String message;
        if (dtos.isEmpty()) message = "조회 결과가 없습니다.";
        else message = String.format("%d건 조회 되었습니다.", dtos.size());

        return new ResponseEntity(CommonResponse.ok(message, dtos), HttpStatus.OK);
    }

}