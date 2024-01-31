package com.example.ptbatch.repository.pass;

import com.example.ptbatch.repository.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@ToString
@Getter
@Entity
@Table(name = "bulk_pass")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BulkPass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bulkPassSeq;
    @Setter
    private Integer packageSeq;
    @Setter
    private String userGroupId;

    @Enumerated(EnumType.STRING)
    @Setter
    private BulkPassStatus status;
    @Setter
    private Integer count;

    @Setter
    private LocalDateTime startedAt;
    @Setter
    private LocalDateTime endedAt;

    private BulkPass(Integer packageSeq, String userGroupId, BulkPassStatus status, Integer count, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.packageSeq = packageSeq;
        this.userGroupId = userGroupId;
        this.status = status;
        this.count = count;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static BulkPass of(Integer packageSeq, String userGroupId, BulkPassStatus status, Integer count, LocalDateTime startedAt, LocalDateTime endedAt) {
        return new BulkPass(packageSeq, userGroupId, status, count, startedAt, endedAt);
    }
}