package com.matzip.api.matzip_api.domain.restrt.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        try {
            restrtService.fetchData();
            return ResponseEntity.ok("fetch data successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while fetching and saving restaurants: " + e.getMessage());
        }
    }
}