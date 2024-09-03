package com.matzip.api.matzip_api.global.notification.service;

import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
import com.matzip.api.matzip_api.domain.restrt.service.RestrtService;
import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.notification.util.DiscordMessageUtil;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final RestrtService restrtService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1276696619034869920/s0XwM3u-ZyFetAH4jz752Usp0nG0Dqz8qdLR_rZs20asUm5IT8j17bQW9mmqvyQsfdzy";
    private static final String AVATAR_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRN9lF93jsUSQ2J5jX4f4OcOvJf4I37mCdrfg&usqp=CAU";

    public void sendUserLunchRecommendation() {
        List<User> userList = userRepository.findUsersWithLunchRecommendationAndLocation();
        for (User user : userList) {
            List<RestrtListByResponseDto> nearbyRestaurants = restrtService.getTop5RestaurantsByRating(
                user.getLat(), user.getLogt(), 0.5);

            if (!nearbyRestaurants.isEmpty()) {
                Map<String, Object> message = DiscordMessageUtil.createDiscordMessage(
                    user.getAccount(), AVATAR_URL, nearbyRestaurants);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(message, headers);

                try {
                    // Discord로 요청 보내기
                    restTemplate.exchange(DISCORD_WEBHOOK_URL, HttpMethod.POST, requestEntity, String.class);
                    log.info("디스코드 알림 전송 성공, 사용자: " + user.getAccount());
                } catch (HttpStatusCodeException ex) {
                    // HTTP 상태 코드에 대한 예외 처리 (4xx, 5xx)
                    log.error("Discord 알림 전송 실패 (HTTP 오류): " + ex.getStatusCode());
                    log.error("오류 메시지: " + ex.getResponseBodyAsString());
                } catch (ResourceAccessException ex) {
                    // 네트워크 연결 오류 또는 타임아웃 예외 처리
                    log.error("Discord 알림 전송 실패 (네트워크 오류): " + ex.getMessage());
                } catch (RestClientException ex) {
                    // 기타 RestTemplate 관련 예외 처리
                    log.error("Discord 알림 전송 실패 (클라이언트 오류): " + ex.getMessage());
                }
            }
        }
    }
}
