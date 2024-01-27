package com.example.ptbatch.job;

import com.example.ptbatch.config.TestBatchConfig;
import com.example.ptbatch.repository.PackageRepositoryTest;
import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassRepository;
import com.example.ptbatch.repository.pass.PassStatus;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {TestBatchConfig.class, ExpirePassesJobConfig.class})
class ExpirePassesJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private PassRepository passRepository;


    @Test
    void expireStep() throws Exception {
        addPassEntities(10);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        assertThat(ExitStatus.COMPLETED).isEqualTo(jobExecution.getExitStatus());
        assertThat("expirePassesJob").isEqualTo(jobInstance.getJobName());
    }

    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<Pass> passEntities = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            Pass entity = Pass.of(1, "A" + 10000 + i, PassStatus.PROGRESSED, random.nextInt(11),
                    now.minusDays(60), now.minusDays(1), null);
            passEntities.add(entity);
        }
        passRepository.saveAllAndFlush(passEntities);
    }

}