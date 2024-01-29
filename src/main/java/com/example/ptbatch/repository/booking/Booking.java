package com.example.ptbatch.repository.booking;

import com.example.ptbatch.repository.BaseEntity;
import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingSeq;
    @Setter
    private Integer passSeq;
    @Setter
    private String userId;

    @Enumerated(EnumType.STRING)
    @Setter
    private BookingStatus status;
    @Setter
    private boolean usedPass;
    @Setter
    private boolean attended;

    @Setter
    private LocalDateTime startedAt;
    @Setter
    private LocalDateTime endedAt;
    @Setter
    private LocalDateTime cancelledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "passSeq", insertable = false, updatable = false)
    private Pass pass;

    public static Booking of(Integer passSeq, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt, User user, Pass pass) {
        return new Booking(passSeq, userId, status, usedPass, attended, startedAt, endedAt, cancelledAt, user, pass);
    }
    private Booking(Integer passSeq, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt, User user, Pass pass) {
        this.passSeq = passSeq;
        this.userId = userId;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
        this.user = user;
        this.pass = pass;
    }
}