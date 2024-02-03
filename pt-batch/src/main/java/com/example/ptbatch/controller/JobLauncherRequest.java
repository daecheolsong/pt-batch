package com.example.ptbatch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

/**
 * @author daecheol song
 * @since 1.0
 */
public record JobLauncherRequest(String name, Properties jobParameters) {

    public JobParameters getJobParameters() {
        return new JobParametersBuilder(jobParameters).toJobParameters();
    }
}
