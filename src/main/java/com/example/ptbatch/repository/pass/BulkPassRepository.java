package com.example.ptbatch.repository.pass;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface BulkPassRepository extends JpaRepository<BulkPass, Integer> {

    List<BulkPass> findByStatusAndStartedAtGreaterThan(BulkPassStatus status, LocalDateTime startedAt);
}
