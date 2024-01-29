package com.example.ptbatch.job;

import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.booking.BookingStatus;
import com.example.ptbatch.repository.notification.Notification;
import com.example.ptbatch.repository.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class SendNotificationBeforeClassJobConfig {
    private static final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    @Bean
    public Job addNotificationBeforeClassJob() {
        return jobBuilderFactory.get("addNotificationBeforeClassJob")
                .start(addNotificationStep())
                .next(sendNotificationStep())
                .build();
    }

    @Bean
    public Step addNotificationStep() {
        return stepBuilderFactory.get("addNotificationStep")
                .<Booking, Notification>chunk(CHUNK_SIZE)
                .reader(addNotificationItemReader())
                .processor(addNotificationItemProcessor())
                .writer(addNotificationItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Booking> addNotificationItemReader() {
        return new JpaPagingItemReaderBuilder<Booking>()
                .name("sendNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select b from Booking b join fetch b.user where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")
                .parameterValues(Map.of("status", BookingStatus.READY, "startedAt", LocalDateTime.now().plusMinutes(10)))
                .build();
    }


    @Bean
    public ItemProcessor<Booking, Notification> addNotificationItemProcessor() {
        return bookingEntity -> Notification.of(
                bookingEntity.getUser().getUUID(),
                NotificationEvent.BEFORE_CLASS,
                String.format("안녕하세요 회원님. %s 수업이 곧 시작합니다. 수업 전 출석 체크 부탁합니다~ \uD83D", bookingEntity.getStartedAt()),
                false,
                null
        );
    }

    @Bean
    public JpaItemWriter<Notification> addNotificationItemWriter() {
        return new JpaItemWriterBuilder<Notification>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step sendNotificationStep() {
        return stepBuilderFactory.get("sendNotificationStep")
                .<Notification, Notification>chunk(CHUNK_SIZE)
                .reader(sendNotificationItemReader())
                .writer(sendNotificationItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public SynchronizedItemStreamReader<Notification> sendNotificationItemReader() {
        return new SynchronizedItemStreamReaderBuilder<Notification>()
                .delegate(new JpaCursorItemReaderBuilder <Notification>()
                        .name("addNotificationItemReader")
                        .entityManagerFactory(entityManagerFactory)
                        .queryString("select n from Notification n where n.event = :event and n.sent = :sent order by n.notificationSeq")
                        .parameterValues(Map.of("event", NotificationEvent.BEFORE_CLASS, "sent", false))
                        .build())
                .build();
    }


}
