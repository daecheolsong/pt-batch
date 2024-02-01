package com.example.ptbatch.repository.statistics;

import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.booking.BookingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author daecheol song
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statisticsSeq;
    @Setter
    private LocalDateTime statisticsAt;
    @Setter
    private int allCount;
    @Setter
    private int attendedCount;
    @Setter
    private int absentCount;
    @Setter
    private int cancelledCount;

    public static Statistics from(final Booking booking) {
        Statistics statisticsEntity = new Statistics();
        statisticsEntity.setStatisticsAt(booking.getStatisticsAt());
        statisticsEntity.setAllCount(1);
        if (booking.isAttended()) {
            statisticsEntity.setAttendedCount(1);
        } else {
            statisticsEntity.setAbsentCount(1);
        }
        if (BookingStatus.CANCELLED.equals(booking.getStatus())) {
            statisticsEntity.setCancelledCount(1);
        }
        return statisticsEntity;

    }

    public void merge(final Statistics statistics) {
        this.allCount += statistics.getAllCount();
        this.attendedCount += statistics.getAttendedCount();
        this.absentCount += statistics.getAbsentCount();
        this.cancelledCount += statistics.getCancelledCount();
    }

}
