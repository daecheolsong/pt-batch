package com.example.ptweb.dto.request;

import com.example.ptbatch.utils.LocalDateTimeUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Data
public class BulkPassRequest {
    private Integer packageSeq;
    private String userGroupId;
    private LocalDateTime startedAt;

    public void setStartedAt(String startedAtString) {
        this.startedAt = LocalDateTimeUtils.parse(startedAtString);
    }
}
