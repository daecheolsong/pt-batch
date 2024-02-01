package com.example.ptbatch.job;

import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.booking.BookingRepository;
import com.example.ptbatch.repository.booking.BookingStatus;
import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class UsePassesJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final PassRepository passRepository;
    private final BookingRepository bookingRepository;
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job usePassesJob() {
        return jobBuilderFactory.get("usePassesJob")
                .start(usePassesStep())
                .build();
    }

    @Bean
    public Step usePassesStep() {
        return stepBuilderFactory.get("usePassesStep")
                .<Booking, Future<Booking>>chunk(CHUNK_SIZE)
                .reader(usePassesItemReader())
                .processor(usePassesAsyncItemProcessor())
                .writer(usePassesAsyncItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Booking> usePassesItemReader() {
        return new JpaCursorItemReaderBuilder<Booking>()
                .name("usePassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Booking b join fetch b.pass where b.status = :status and b.usedPass = false and b.endedAt <= :endedAt")
                .parameterValues(Map.of("status", BookingStatus.COMPLETED, "endedAt", LocalDateTime.now()))
                .build();
    }

    @Bean
    public AsyncItemProcessor<Booking, Booking> usePassesAsyncItemProcessor() {
        AsyncItemProcessor<Booking, Booking> usePassesAsyncItemProcessor = new AsyncItemProcessor<>();
        usePassesAsyncItemProcessor.setDelegate(usePassesItemProcessor());
        usePassesAsyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return usePassesAsyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Booking, Booking> usePassesItemProcessor() {
        return booking -> {
            Pass pass = booking.getPass();
            pass.setRemainingCount(pass.getRemainingCount() - 1);
            booking.setPass(pass);
            booking.setUsedPass(true);
            return booking;
        };
    }

    @Bean
    public AsyncItemWriter<Booking> usePassesAsyncItemWriter() {
        AsyncItemWriter<Booking> usePassesAsyncItemWriter = new AsyncItemWriter<>();
        usePassesAsyncItemWriter.setDelegate(usePassesItemWriter());
        return usePassesAsyncItemWriter;
    }

    @Bean
    public ItemWriter<Booking> usePassesItemWriter() {
        return bookings -> bookings
                .forEach(booking -> {
                    passRepository.save(booking.getPass());
                    bookingRepository.save(booking);
                });
    }

}
