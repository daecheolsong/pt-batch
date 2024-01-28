package com.example.ptbatch.job;

import com.example.ptbatch.config.TestBatchConfig;
import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.repository.pass.BulkPass;
import com.example.ptbatch.repository.pass.BulkPassRepository;
import com.example.ptbatch.repository.pass.BulkPassStatus;
import com.example.ptbatch.repository.user.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBatchTest
@SpringBootTest(classes = {AddPassesJobConfig.class, TestBatchConfig.class, TestJpaConfig.class, AddPassesTasklet.class})
@ActiveProfiles("test")
public class AddPassesJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private BulkPassRepository bulkPassRepository;
    @Autowired
    private UserGroupMappingRepository userGroupMappingRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void test_addPassesJob() throws Exception {
        addBulkPassEntity();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        assertThat(ExitStatus.COMPLETED).isEqualTo(jobExecution.getExitStatus());
        assertThat("addPassesJob").isEqualTo(jobInstance.getJobName());
    }

    private void addBulkPassEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userGroupId = "testGroup-" + RandomStringUtils.randomAlphabetic(6);
        final String userId = "A100" + RandomStringUtils.randomNumeric(4);
        final String userName = "test-" + RandomStringUtils.randomAlphabetic(10);


        User user = User.of(userId, userName, UserStatus.ACTIVE, "010-xxx-xxxx", "test metadata");
        userRepository.save(user);

        UserGroup userGroup = UserGroup.of(userGroupId, "test-group", "test-description");
        userGroupRepository.save(userGroup);

        UserGroupMapping userGroupMappingEntity = UserGroupMapping.of(userGroup.getUserGroupId(), user.getUserId());
        userGroupMappingRepository.save(userGroupMappingEntity);

        BulkPass bulkPassEntity = BulkPass.of(1, userGroup.getUserGroupId(), BulkPassStatus.READY, 10, now, now.plusDays(60));
        bulkPassRepository.save(bulkPassEntity);
    }

}