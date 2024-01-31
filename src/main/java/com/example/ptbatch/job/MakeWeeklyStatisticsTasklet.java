package com.example.ptbatch.job;

import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.statistics.AggregatedStatistics;
import com.example.ptbatch.repository.statistics.Statistics;
import com.example.ptbatch.repository.statistics.StatisticsRepository;
import com.example.ptbatch.utils.CustomCSVWriter;
import com.example.ptbatch.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Component
@StepScope
public class MakeWeeklyStatisticsTasklet implements Tasklet {

    @Value("#{jobParameters[from]}")
    private String fromString;

    private final StatisticsRepository statisticsRepository;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final LocalDateTime from = LocalDateTimeUtils.parse(fromString);
        final LocalDateTime to = Objects.requireNonNull(from).plusDays(7);

        final List<AggregatedStatistics> statisticsList = statisticsRepository.findByStatisticsAtBetweenAndGroupBy(from, to);

        final LinkedHashMap<Integer, AggregatedStatistics> weeklyStatisticsEntityMap = statisticsList.stream()
                .collect(Collectors.toMap(
                        statistics -> LocalDateTimeUtils.getWeekOfYear(statistics.getStatisticsAt()),
                        Function.identity(),
                        (accumulator, element) -> {
                            accumulator.merge(element);
                            return accumulator;
                        },
                        LinkedHashMap::new
                ));

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"week", "allCount", "attendedCount", "absentCount", "cancelledCount"});
        weeklyStatisticsEntityMap.forEach((week, statistics) -> {
            data.add(new String[]{
                    "Week " + week,
                    String.valueOf(statistics.getAllCount()),
                    String.valueOf(statistics.getAttendedCount()),
                    String.valueOf(statistics.getAbsentCount()),
                    String.valueOf(statistics.getCancelledCount())
            });

        });
        CustomCSVWriter.write("weekly_statistics_" + LocalDateTimeUtils.format(from, LocalDateTimeUtils.YYYY_MM_DD) + ".csv", data);
        return RepeatStatus.FINISHED;

    }
}