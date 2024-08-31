package com.matzip.api.matzip_api.domain.sgg.contoller;


import com.matzip.api.matzip_api.domain.sgg.service.SggService;
import com.matzip.api.matzip_api.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sgg")
@RequiredArgsConstructor
public class SggController {

    private final SggService sggService;

    @GetMapping()
    public ResponseEntity<?> getSggList(){
        return new ResponseEntity<>(CommonResponse.ok(sggService.getSggList()), HttpStatus.OK);
    }
}
