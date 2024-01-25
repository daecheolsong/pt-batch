package com.example.ptbatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@EnableBatchProcessing
@SpringBootApplication
@Slf4j
public class PtBatchApplication {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Step passStep() {
        return stepBuilderFactory.get("passStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("PASS STEP >>>>>>>>> Execute PassStep");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job passJob() {
        return jobBuilderFactory.get("passJob")
                .start(passStep())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PtBatchApplication.class, args);
    }

}
