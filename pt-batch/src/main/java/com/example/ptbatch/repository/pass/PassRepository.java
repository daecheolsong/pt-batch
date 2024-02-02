package com.example.ptbatch.repository.pass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface PassRepository extends JpaRepository<Pass, Integer> {
    @Query(value = "select p from Pass p " +
            "join fetch p.packaze " +
            "where p.userId = :userId " +
            "order by p.endedAt desc nulls first ")
    List<Pass> findByUserId(String userId);
}
