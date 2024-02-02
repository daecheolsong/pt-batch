package com.example.ptweb.service.statistics;

import com.example.ptbatch.repository.statistics.AggregatedStatistics;
import com.example.ptbatch.repository.statistics.StatisticsRepository;
import com.example.ptweb.dto.response.AggregatesStatisticsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;
    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void test_getAggregatesStatisticsList() {
        LocalDateTime to = LocalDateTime.of(2022, 9, 10, 0, 0);
        List<AggregatedStatistics> aggregatedStatisticsList = List.of(
                new AggregatedStatistics(to.minusDays(2), (long) 15, (long) 10, (long) 5, (long) 0),
                new AggregatedStatistics(to.minusDays(1), (long) 20, (long) 10, (long) 5, (long) 5)
        );
        when(statisticsRepository.findByStatisticsAtBetweenAndGroupBy(eq(to.minusDays(10)), eq(to))).thenReturn(aggregatedStatisticsList);

        AggregatesStatisticsResponse actual = statisticsService.getAggregatesStatisticsList(to);

        then(statisticsRepository).should(times(1)).findByStatisticsAtBetweenAndGroupBy(eq(to.minusDays(10)), eq(to));
        assertThat(actual).isNotNull();
        assertThat(actual.getLabels()).isEqualTo(List.of("09-08", "09-09"));
        assertThat(actual.getAttendedCounts()).isEqualTo(List.of(10L, 10L));
        assertThat(actual.getCancelledCounts()).isEqualTo(List.of(0L, 5L));
    }
}