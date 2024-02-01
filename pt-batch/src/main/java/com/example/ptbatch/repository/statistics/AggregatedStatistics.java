package com.example.ptbatch.repository.statistics;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
public class AggregatedStatistics {
    private final LocalDateTime statisticsAt;
    private Long allCount;
    private Long attendedCount;
    private Long absentCount;
    private Long cancelledCount;

    public void merge(AggregatedStatistics statistics) {
        this.allCount += statistics.getAllCount();
        this.attendedCount += statistics.getAttendedCount();
        this.absentCount += statistics.getAbsentCount();
        this.cancelledCount += statistics.getCancelledCount();
    }

    public AggregatedStatistics(LocalDateTime statisticsAt, Long allCount, Long attendedCount, Long absentCount, Long cancelledCount) {
        this.statisticsAt = statisticsAt;
        this.allCount = allCount;
        this.attendedCount = attendedCount;
        this.absentCount = absentCount;
        this.cancelledCount = cancelledCount;
    }
}
