package com.example.ptbatch.repository.booking;

import com.example.ptbatch.repository.BaseEntity;
import com.example.ptbatch.repository.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingSeq;
    private Integer passSeq;
    private String userId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private boolean usedPass;
    private boolean attended;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime cancelledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public static Booking of(Integer passSeq, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt, User user) {
        return new Booking(passSeq, userId, status, usedPass, attended, startedAt, endedAt, cancelledAt, user);
    }
    private Booking(Integer passSeq, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt, User user) {
        this.passSeq = passSeq;
        this.userId = userId;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
        this.user = user;
    }
}