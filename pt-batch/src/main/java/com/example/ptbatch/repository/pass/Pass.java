package com.example.ptbatch.repository.pass;

import com.example.ptbatch.repository.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Entity
@Table(name = "pass")
public class Pass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passSeq;
    @Setter
    private Integer packageSeq;
    @Setter
    private String userId;

    @Setter
    @Enumerated(EnumType.STRING)
    private PassStatus status;
    @Setter
    private Integer remainingCount;
    @Setter
    private LocalDateTime startedAt;
    @Setter
    private LocalDateTime endedAt;
    @Setter
    private LocalDateTime expiredAt;


    private Pass(Integer packageSeq, String userId, PassStatus status, Integer remainingCount,
                LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime expiredAt) {
        this.packageSeq = packageSeq;
        this.userId = userId;
        this.status = status;
        this.remainingCount = remainingCount;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.expiredAt = expiredAt;
    }

    public static Pass of(Integer packageSeq, String userId, PassStatus status, Integer remainingCount,
                          LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime expiredAt) {
        return new Pass(packageSeq, userId, status, remainingCount, startedAt, endedAt, expiredAt);
    }
}
