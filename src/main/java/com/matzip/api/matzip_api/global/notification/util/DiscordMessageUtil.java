package com.matzip.api.matzip_api.global.notification.util;

import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DiscordMessageUtil {
    /**
     * Discord 웹훅 메시지 본문을 생성하는 유틸 메서드
     *
     * @param username 사용자 이름
     * @param avatarUrl 사용자 아바타 URL
     * @param restaurants 맛집 리스트 (상위 5개)
     * @return Discord 웹훅 메시지 본문
     */
    public static Map<String, Object> createDiscordMessage(String username, String avatarUrl, List<RestrtListByResponseDto> restaurants) {
        // 메시지 기본 정보 설정
        Map<String, Object> message = new HashMap<>();
        message.put("username", username);
        message.put("avatar_url", avatarUrl);
        message.put("content", "Your LunchHere! 오늘의 점심 추천 맛집은~");

        // embed 구성
        Map<String, Object> embed = new HashMap<>();
        embed.put("author", Map.of(
            "name", "Lunch Recommendation",
            "icon_url", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRN9lF93jsUSQ2J5jX4f4OcOvJf4I37mCdrfg&usqp=CAU"
        ));
        embed.put("description", "사용자 위치에서 500m 이내의 추천 맛집 리스트:");
        embed.put("color", 37411);

        // 맛집 리스트를 Discord 필드 형식으로 추가
        embed.put("fields", createRestaurantFields(restaurants));

        // footer 추가
        embed.put("footer", Map.of(
            "text", "언제나 당신을 위한 맛집과 함께 돌아올게요, Enjoy your LunchHere :)",
            "icon_url", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRN9lF93jsUSQ2J5jX4f4OcOvJf4I37mCdrfg&usqp=CAU"
        ));

        // embeds 리스트로 추가
        message.put("embeds", List.of(embed));
        return message;
    }

    /**
     * 맛집 리스트를 Discord 필드 형식으로 변환
     *
     * @param restaurants 맛집 리스트
     * @return 필드 리스트
     */
    private static List<Map<String, String>> createRestaurantFields(
        List<RestrtListByResponseDto> restaurants) {
        return restaurants.stream()
            .map(restaurant -> Map.of(
                "name", ":fork_and_knife: " + restaurant.getBizplc_nm(),
                "value", restaurant.getRefine_roadnm_addr()
            ))
            .toList();
    }
}
