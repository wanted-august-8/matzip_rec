package com.matzip.api.matzip_api.domain.restrt.controller;

import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class RestrtController {
    @Autowired
    private RestrtService restrtService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData(@RequestBody String json) throws Exception {
        restrtService.fetchData();
        return ResponseEntity.ok("fetch data successfully.");
    }
}