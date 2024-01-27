package com.example.ptbatch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableBatchProcessing
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableJpaRepositories("com.example.ptbatch.repository")
@EntityScan("com.example.ptbatch.repository")
@EnableTransactionManagement
public class TestBatchConfig {

}