package com.example.ptweb.dto;

import com.example.ptbatch.repository.pass.BulkPass;
import com.example.ptbatch.repository.pass.BulkPassStatus;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
public record BulkPassDto(
        Integer bulkPassSeq,
         String userGroupId,
         Integer count,
         BulkPassStatus status,
         LocalDateTime startedAt,
         LocalDateTime endedAt
) {
    public static BulkPassDto from(BulkPass entity) {
        return new BulkPassDto(
                entity.getBulkPassSeq(),
                entity.getUserGroupId(),
                entity.getCount(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getEndedAt(
                )
        );
    }
}
