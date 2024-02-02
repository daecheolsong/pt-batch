package com.example.ptweb.dto;

import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public class PassDto {
    private Integer passSeq;
    private Integer packageSeq;
    private String packageName;
    private String userId;

    private PassStatus status;
    private Integer remainingCount;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime expiredAt;

    public static PassDto from(Pass entity) {
        return new PassDto(
                entity.getPassSeq(),
                entity.getPackageSeq(),
                entity.getPackaze().getPackageName(),
                entity.getUserId(),
                entity.getStatus(),
                entity.getRemainingCount(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getExpiredAt()
        );
    }

}
