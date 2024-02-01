package com.example.ptbatch.job;

import com.example.ptbatch.adapter.message.KakaoTalkMessageAdapter;
import com.example.ptbatch.repository.notification.Notification;
import com.example.ptbatch.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daecheol song
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SendNotificationItemWriter implements ItemWriter<Notification> {

    private final NotificationRepository notificationRepository;
    private final KakaoTalkMessageAdapter kakaoTalkMessageAdapter;

    @Override
    public void write(List<? extends Notification> items) throws Exception {
        AtomicInteger count = new AtomicInteger();

        items.stream()
                .filter(item ->
                        kakaoTalkMessageAdapter.sendKakaoTalkMessage(item.getUuid(), item.getText()))
                .forEach(
                        notification -> {
                            notification.setSent(true);
                            notification.setSentAt(LocalDateTime.now());
                            notificationRepository.save(notification);
                            count.getAndIncrement();
                        }
                );
        log.info("SendNotificationWriter - write : 수업 전 알람 {} / {}건 전송 성공", count.get(), items.size());
    }
}
