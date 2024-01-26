package com.example.ptbatch.repository.packze;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface PackageRepository extends JpaRepository<Package, Integer> {
    List<Package> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);

}
