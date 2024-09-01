package com.matzip.api.matzip_api.domain.restrt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<RestrtDTO> fetchAllData(String category) throws Exception {
        List<RestrtDTO> allRestaurants = new ArrayList<>();
        int pIndex = 1;
        int pSize = 1000;
        boolean hasMore = true;

        while (hasMore) {
            String jsonResponse = fetchData(category, pIndex, pSize);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path(category).path(1).path("row");

            if (items.isEmpty()) {
                hasMore = false;
            } else {
                for (JsonNode item : items) {
                    RestrtDTO restaurant = objectMapper.treeToValue(item, RestrtDTO.class);
                    allRestaurants.add(restaurant);
                }
                pIndex++;
            }
        }

        return allRestaurants;
    }

    private String fetchData(String category, int pIndex, int pSize) {
        String url = String.format(
            "https://openapi.gg.go.kr/%s?Type=json&key=%s&pIndex=%d&pSize=%d",
            category, "46bf683916264de0b22ec4968c3f89d3", pIndex, pSize);
        return restTemplate.getForObject(url, String.class);
    }
}
