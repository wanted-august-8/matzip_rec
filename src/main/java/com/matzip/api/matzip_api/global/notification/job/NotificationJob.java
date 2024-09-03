package com.matzip.api.matzip_api.global.notification.job;

import com.matzip.api.matzip_api.global.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationJob {
    private final NotificationService notificationService;

    // 매주 월요일부터 금요일까지 오전 11시 30분에 알림 전송
    @Scheduled(cron = "0 30 11 ? * MON-FRI", zone = "Asia/Seoul")
    public void sendDailyNotification() {
        notificationService.sendUserLunchRecommendation();
    }
}
