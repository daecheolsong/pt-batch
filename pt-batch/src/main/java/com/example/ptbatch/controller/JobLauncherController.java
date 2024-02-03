package com.example.ptbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/job")
public class JobLauncherController {

    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    @PostMapping("/launcher")
    public ExitStatus launchJob(@RequestBody JobLauncherRequest request) throws Exception {
        Job job = jobRegistry.getJob(request.name());
        return jobLauncher.run(job, new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(job).toJobParameters()).getExitStatus();
    }
}
