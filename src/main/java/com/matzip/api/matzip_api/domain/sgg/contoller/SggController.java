package com.matzip.api.matzip_api.domain.sgg.contoller;


import static com.matzip.api.matzip_api.global.error.ErrorCode.INVALID_FILE_FORMAT;

import com.matzip.api.matzip_api.domain.sgg.service.SggService;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.auth.domain.CustomUserDetails;
import com.matzip.api.matzip_api.global.error.ErrorResponse;
import com.matzip.api.matzip_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sgg")
@RequiredArgsConstructor
public class SggController {

    private final SggService sggService;

    @Operation(summary = "시군구 목록 조회", description = "시/도별 시군구 목록을 조회할 수 있습니다.")
    @GetMapping()
    public ResponseEntity<?> getSggList(){
        return new ResponseEntity<>(CommonResponse.ok(sggService.getSggList()), HttpStatus.OK);
    }

    @Operation(summary = "시군구 csv 파일을 DB에 저장", description = "시군구 csv 파일을 DB에 저장합니다.")
    @PostMapping("/upload/csv")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".csv")) {
            throw new CustomException(INVALID_FILE_FORMAT);
        }
        sggService.uploadFile(file);
        return new ResponseEntity<>(CommonResponse.ok("저장되었습니다.", null),HttpStatus.OK);
    }
}
