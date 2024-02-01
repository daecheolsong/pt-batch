package com.example.ptbatch.job;

import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.statistics.Statistics;
import com.example.ptbatch.repository.statistics.StatisticsRepository;
import com.example.ptbatch.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author daecheol song
 * @since 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MakeStatisticsJobConfig {

    private static final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final StatisticsRepository statisticsRepository;
    private final MakeDailyStatisticsTasklet makeDailyStatisticsTasklet;
    private final MakeWeeklyStatisticsTasklet makeWeeklyStatisticsTasklet;


    @Bean
    public Job makeStatisticsJob() {
        Flow addStatisticsFlow = new FlowBuilder<Flow>("addStatisticsFlow")
                .start(addStatisticsStep())
                .build();

        Flow makeDailyStatisticsFlow = new FlowBuilder<Flow>("makeDailyStatisticsFlow")
                .start(makeDailyStatisticsStep())
                .build();

        Flow makeWeeklyStatisticsFlow = new FlowBuilder<Flow>("makeWeeklyStatisticsFlow")
                .start(makeWeeklyStatisticsStep())
                .build();

        Flow parallelMakeStatisticsFlow = new FlowBuilder<Flow>("parallelMakeStatisticsFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(makeDailyStatisticsFlow, makeWeeklyStatisticsFlow)
                .build();

        return jobBuilderFactory.get("makeStatisticsJob")
                .listener(clearStatisticsRepository())
                .start(addStatisticsFlow)
                .next(parallelMakeStatisticsFlow)
                .build()
                .build();

    }

    @Bean
    @JobScope
    public JobExecutionListener clearStatisticsRepository() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                statisticsRepository.deleteAllInBatch();
                log.info("clear saved statistics before, avoid duplicate");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

            }
        };
    }

    @Bean
    public Step addStatisticsStep() {
        return this.stepBuilderFactory.get("addStatisticsStep")
                .<Booking, Booking>chunk(CHUNK_SIZE)
                .reader(addStatisticsItemReader(null, null))
                .writer(addStatisticsItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<Booking> addStatisticsItemReader(@Value("#{jobParameters[from]}") String fromString, @Value("#{jobParameters[to]}") String toString) {
        final LocalDateTime from = LocalDateTimeUtils.parse(fromString);
        final LocalDateTime to = LocalDateTimeUtils.parse(toString);

        return new JpaCursorItemReaderBuilder<Booking>()
                .name("usePassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Booking b where b.endedAt between :from and :to")
                .parameterValues(Map.of("from", from, "to", to))
                .build();
    }

    @Bean
    public ItemWriter<Booking> addStatisticsItemWriter() {
        return bookingEntities -> {

            LinkedHashMap<LocalDateTime, Statistics> statisticsEntityMap = bookingEntities.stream()
                    .collect(Collectors.toMap(
                            Booking::getStatisticsAt,
                            Statistics::from,
                            (accumulator, element) -> {
                                accumulator.merge(element);
                                return accumulator;
                            },
                            LinkedHashMap::new
                    ));

            final List<Statistics> statisticsEntities = new ArrayList<>(statisticsEntityMap.values());
            statisticsRepository.saveAll(statisticsEntities);
            log.info("### addStatisticsStep 종료");

        };
    }

    @Bean
    public Step makeDailyStatisticsStep() {
        return this.stepBuilderFactory.get("makeDailyStatisticsStep")
                .tasklet(makeDailyStatisticsTasklet)
                .build();
    }

    @Bean
    public Step makeWeeklyStatisticsStep() {
        return this.stepBuilderFactory.get("makeWeeklyStatisticsStep")
                .tasklet(makeWeeklyStatisticsTasklet)
                .build();
    }



}
