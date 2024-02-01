package com.example.ptbatch.job;

import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ExpirePassesJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    public static final int CHUNK_SIZE = 5;
    @Bean
    public Job expirePassesJob() {
        return jobBuilderFactory.get("expirePassesJob")
                .start(expirePassesStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step expirePassesStep() {
        return stepBuilderFactory.get("expirePassesStep")
                .<Pass, Pass>chunk(CHUNK_SIZE)
                .reader(expirePassesItemReader())
                .processor(expirePassesItemProcessor())
                .writer(expirePassesItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<Pass> expirePassesItemReader() {
        return new JpaCursorItemReaderBuilder<Pass>()
                .name("expirePassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Pass p "
                        + "WHERE p.status = :status AND p.endedAt <= :endedAt")
                .parameterValues(Map.of("status", PassStatus.PROGRESSED
                        , "endedAt", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<Pass, Pass> expirePassesItemProcessor() {
        return entity -> {
            entity.setStatus(PassStatus.EXPIRED);
            entity.setExpiredAt(LocalDateTime.now());
            return entity;
        };
    }

    @Bean
    public JpaItemWriter<Pass> expirePassesItemWriter() {
        return new JpaItemWriterBuilder<Pass>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
