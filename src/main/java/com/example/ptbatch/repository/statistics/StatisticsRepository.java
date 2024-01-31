package com.example.ptbatch.repository.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {

    @Query(value = "SELECT new com.example.ptbatch.repository.statistics.AggregatedStatistics(s.statisticsAt, SUM(s.allCount), SUM(s.attendedCount), SUM(s.absentCount), SUM(s.cancelledCount)) " +
            "         FROM Statistics s " +
            "        WHERE s.statisticsAt BETWEEN :from AND :to " +
            "     GROUP BY s.statisticsAt")
    List<AggregatedStatistics> findByStatisticsAtBetweenAndGroupBy(LocalDateTime from, LocalDateTime to);
}
