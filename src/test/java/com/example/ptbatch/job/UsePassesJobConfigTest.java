package com.example.ptbatch.job;

import com.example.ptbatch.config.TestBatchConfig;
import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.booking.BookingRepository;
import com.example.ptbatch.repository.booking.BookingStatus;
import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassRepository;
import com.example.ptbatch.repository.pass.PassStatus;
import com.example.ptbatch.repository.user.User;
import com.example.ptbatch.repository.user.UserRepository;
import com.example.ptbatch.repository.user.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, TestJpaConfig.class,
UsePassesJobConfig.class})
public class UsePassesJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private PassRepository passRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void usePassesStep() throws Exception {
        createTestEntities();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        ExitStatus exitStatus = jobExecution.getExitStatus();
        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
    }


    private void createTestEntities() {
        String userId = "testId-" + RandomStringUtils.randomAlphabetic(6);
        String userName = "tester-" + RandomStringUtils.randomAlphabetic(8);

        User user = User.of(
                userId,
                userName,
                UserStatus.ACTIVE,
                "010-0230-0000",
                Map.of()
        );

        userRepository.save(user);

        Pass pass = Pass.of(
                1,
                user.getUserId(),
                PassStatus.PROGRESSED,
                10,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(60),
                null
        );
        passRepository.save(pass);

        Booking booking = Booking.of(
                pass.getPassSeq(),
                user.getUserId(),
                BookingStatus.COMPLETED,
                false,
                true,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusMinutes(10),
                null,
                null,
                null
        );
        bookingRepository.save(booking);
    }

}