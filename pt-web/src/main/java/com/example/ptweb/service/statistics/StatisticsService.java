package com.example.ptweb.service.statistics;

import com.example.ptbatch.repository.statistics.StatisticsRepository;
import com.example.ptweb.dto.response.AggregatesStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;


    public AggregatesStatisticsResponse getAggregatesStatisticsList(final LocalDateTime to) {
        final LocalDateTime from = to.minusDays(10);

        return AggregatesStatisticsResponse.from(statisticsRepository.findByStatisticsAtBetweenAndGroupBy(from, to));
    }


}
