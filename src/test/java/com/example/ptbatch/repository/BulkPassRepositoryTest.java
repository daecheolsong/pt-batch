package com.example.ptbatch.repository;

import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.repository.pass.BulkPass;
import com.example.ptbatch.repository.pass.BulkPassRepository;
import com.example.ptbatch.repository.pass.BulkPassStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestJpaConfig.class)
@ActiveProfiles("test")
public class BulkPassRepositoryTest {

    @Autowired
    BulkPassRepository bulkPassRepository;

    @Test
    void findByStatusAndStartedAtGreaterThan() {
        BulkPass savedEntity = addEntity();
        LocalDateTime datetime = LocalDateTime.now();
        BulkPassStatus status = BulkPassStatus.READY;

        List<BulkPass> actual = bulkPassRepository.findByStatusAndStartedAtGreaterThan(status, datetime);

        assertThat(actual).hasSize(1);
        assertThat(actual).extracting("status", BulkPassStatus.class)
                .containsExactly(status);
        assertThat(actual).extracting("startedAt", LocalDateTime.class)
                .allSatisfy(startedAt -> assertThat(startedAt).isAfter(datetime));


    }

    private BulkPass addEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userGroupId = RandomStringUtils.randomAlphabetic(6);

        BulkPass entity = BulkPass.of(1, userGroupId, BulkPassStatus.READY, 10, now.plusMinutes(3), now.plusDays(60));
        return bulkPassRepository.save(entity);
    }
}