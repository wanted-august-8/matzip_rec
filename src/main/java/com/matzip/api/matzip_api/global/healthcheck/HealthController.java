package com.matzip.api.matzip_api.global.healthcheck;

import com.matzip.api.matzip_api.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "API to check the application status")
public class HealthController {
    /**
     * Application 상태 확인 컨트롤러
     */
    @Operation(
        summary = "Check application status",
        description = "Checks if the application is running correctly"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is running normally."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/health-check")
    public ResponseEntity<CommonResponse<Object>> getAppHealth() {
        CommonResponse<Object> ok = CommonResponse.ok("애플리케이션이 정상적으로 동작하고 있습니다", null);
        return new ResponseEntity<>(ok, HttpStatusCode.valueOf(200));
    }
}