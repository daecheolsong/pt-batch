package com.example.ptbatch.job;

import com.example.ptbatch.adapter.message.KakaoTalkMessageAdapter;
import com.example.ptbatch.config.KakaoMessageConfig;
import com.example.ptbatch.config.TestBatchConfig;
import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.config.WebClientConfig;
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
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest(classes = {SendNotificationBeforeClassJobConfig.class,
        TestBatchConfig.class, TestJpaConfig.class, SendNotificationItemWriter.class,
KakaoMessageConfig.class, KakaoTalkMessageAdapter.class, WebClientConfig.class})
public class SendNotificationBeforeClassJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void addNotificationStep() {
        addBookingEntity();

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addNotificationStep");

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private void addBookingEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A100" + RandomStringUtils.randomNumeric(4);
        final String userName = RandomStringUtils.randomAlphabetic(8);
        User user = User.of(userId, userName, UserStatus.ACTIVE, "010-2323-1111", Map.of("uuid", "avsdv23"));
        userRepository.save(user);

        Pass pass = Pass.of(1, user.getUserId(), PassStatus.PROGRESSED, 10, now.minusDays(60), now.minusDays(1), null);
        passRepository.save(pass);

        Booking booking = Booking.of(pass.getPassSeq(), user.getUserId(), BookingStatus.READY, false, false, now.plusMinutes(10), now.plusMinutes(60), null, null);
        bookingRepository.save(booking);

    }

}