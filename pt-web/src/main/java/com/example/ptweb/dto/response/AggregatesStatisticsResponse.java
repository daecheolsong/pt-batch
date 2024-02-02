package com.example.ptweb.dto.response;

import com.example.ptbatch.repository.statistics.AggregatedStatistics;
import com.example.ptbatch.utils.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public class AggregatesStatisticsResponse {

    private final List<String> labels;
    private final List<Long> attendedCounts;
    private final List<Long> cancelledCounts;

    public static AggregatesStatisticsResponse from (List<AggregatedStatistics> aggregatedStatisticsList) {
        List<String> labels = aggregatedStatisticsList.stream()
                .map(aggregatedStatistics ->
                        LocalDateTimeUtils.format(aggregatedStatistics.getStatisticsAt(), LocalDateTimeUtils.MM_DD))
                .toList();
        List<Long> attendedCounts = aggregatedStatisticsList.stream().map(AggregatedStatistics::getAttendedCount)
                .toList();
        List<Long> cancelledCounts = aggregatedStatisticsList.stream().map(AggregatedStatistics::getCancelledCount)
                .toList();

        return new AggregatesStatisticsResponse(labels, attendedCounts, cancelledCounts);
    }
}
