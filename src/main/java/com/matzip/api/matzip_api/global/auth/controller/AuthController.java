package com.matzip.api.matzip_api.global.auth.controller;

import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.auth.dto.SignUpRequestDto;
import com.matzip.api.matzip_api.global.auth.service.AuthService;
import com.matzip.api.matzip_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     *  사용자 회원가입
     *
     * @param signUpRequestDto 회원가입 요청 DTO
     * @return 사용자 Id
     * @throws CustomException 계정명 중복 -> ACCOUNT_ALREADY_REGISTERED
     */
    @Operation(summary = "사용자 회원가입", description = "사용자는 계정과 비밀번호로 회원 가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Long>> signUp(
        @RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        CommonResponse<Long> response = CommonResponse.ok("회원가입에 성공하였습니다.",
            authService.signUp(signUpRequestDto));

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
