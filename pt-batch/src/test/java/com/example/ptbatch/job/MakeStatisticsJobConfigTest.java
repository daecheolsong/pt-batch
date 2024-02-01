package com.example.ptbatch.job;

import com.example.ptbatch.config.TestBatchConfig;
import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.repository.booking.Booking;
import com.example.ptbatch.repository.booking.BookingRepository;
import com.example.ptbatch.repository.booking.BookingStatus;
import com.example.ptbatch.repository.packze.Package;
import com.example.ptbatch.repository.packze.PackageRepository;
import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassRepository;
import com.example.ptbatch.repository.pass.PassStatus;
import com.example.ptbatch.repository.user.User;
import com.example.ptbatch.repository.user.UserRepository;
import com.example.ptbatch.repository.user.UserStatus;
import com.example.ptbatch.utils.LocalDateTimeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestJpaConfig.class, TestBatchConfig.class, MakeStatisticsJobConfig.class,
MakeDailyStatisticsTasklet.class, MakeWeeklyStatisticsTasklet.class})
@SpringBatchTest
public class MakeStatisticsJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private PassRepository passRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void makeStatisticsJobTest() throws Exception {

        makeEntities(LocalDateTime.of(2023, Month.APRIL, 15, 0, 0));
        long runId = new Random().nextLong(0, 1_000_000_000);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("from", LocalDateTime.of(2023, Month.MAY, 20, 0, 0).format(LocalDateTimeUtils.YYYY_MM_DD_HH_MM))
                .addString("to", LocalDateTime.of(2023, Month.MAY, 27, 0, 0).format(LocalDateTimeUtils.YYYY_MM_DD_HH_MM))
                .addLong("run.id", runId)
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private void makeEntities(LocalDateTime from) {
        String[] insertedUserId = {"A1000001", "A1000002", "B1000010",
                "B1000011", "B2000000", "B2000001"};

        List<Package> insertedPackages = packageRepository.findAll();

        List<Pass> testPasses = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i <= 100; i++) {
            int rPick = r.nextInt(6);
            String pickedUserId = insertedUserId[rPick];
            rPick = r.nextInt(7);
            Package pickedPackage = insertedPackages.get(rPick);

            LocalDateTime startedAt = from.plusDays(r.nextInt(30));
            LocalDateTime endedAt = startedAt.plusDays(r.nextInt(30, 60));
            Pass pass = Pass.of(pickedPackage.getPackageSeq(),
                    pickedUserId,
                    PassStatus.PROGRESSED,
                    r.nextInt(1, 20),
                    startedAt,
                    endedAt,
                    null
            );
            testPasses.add(pass);
        }


        List<Pass> passes = passRepository.saveAll(testPasses);


        List<Booking> testBookings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            int rPick = r.nextInt(passes.size());
            Pass pass = passes.get(rPick);
            LocalDateTime bookStartedAt = pass.getEndedAt().minusDays(r.nextInt(30));
            Booking booking = Booking.of(
                    pass.getPassSeq(),
                    pass.getUserId(),
                    BookingStatus.COMPLETED,
                    true,
                    r.nextInt(5) > 3,
                    bookStartedAt,
                    bookStartedAt.plusMinutes(r.nextInt(60, 90)),
                    null,
                    null,
                    null
            );

            testBookings.add(booking);
        }

        bookingRepository.saveAll(testBookings);

    }

}