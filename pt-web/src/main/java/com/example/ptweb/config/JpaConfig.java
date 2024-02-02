package com.example.ptweb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = {"com.example.ptbatch.repository"})
@EnableJpaRepositories(basePackages = {"com.example.ptbatch.repository"})
public class JpaConfig {
}
