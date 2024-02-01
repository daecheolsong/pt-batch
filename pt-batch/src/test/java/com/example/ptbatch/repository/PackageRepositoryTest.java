package com.example.ptbatch.repository;

import com.example.ptbatch.config.TestJpaConfig;
import com.example.ptbatch.repository.packze.Package;
import com.example.ptbatch.repository.packze.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import(TestJpaConfig.class)
@ActiveProfiles("test")
public class PackageRepositoryTest {
    private final PackageRepository packageRepository;

    public PackageRepositoryTest(@Autowired PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }


    @Test
    @DisplayName("save 테스트")
    void save() {
        Package entity = Package.of("바디 챌린지 PT 12주", null, 84);
        long previousCount = packageRepository.count();

        packageRepository.save(entity);

        assertThat(entity.getPackageSeq()).isNotNull();
        assertThat(packageRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    void findByCreatedAfter() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
        Package package1 = Package.of("학생 전용 3개월", null, 90);
        Package package2 = Package.of("학생 전용 6개월", null, 180);
        packageRepository.save(package1);
        packageRepository.save(package2);

        List<Package> actual = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 2, Sort.by("packageSeq").descending()));

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getPackageSeq()).isEqualTo(package2.getPackageSeq());
        assertThat(actual).extracting("createdAt", LocalDateTime.class)
                .allSatisfy(createdAt -> assertThat(createdAt).isAfter(dateTime));

    }

    @Test
    void update() {

        Package entity = Package.of("바디프로필 이벤트 4개월", null, 90);
        packageRepository.save(entity);
        int updatedCount = 30;
        int updatedPeriod = 120;

        entity.setCount(updatedCount);
        entity.setPeriod(updatedPeriod);
        packageRepository.flush();
        Package updatedEntity = packageRepository.save(entity);

        assertThat(entity).isSameAs(updatedEntity);
        assertThat(updatedEntity.getCount()).isEqualTo(updatedCount);
        assertThat(updatedEntity.getPeriod()).isEqualTo(updatedPeriod);

    }

    @Test
    void delete() {
        Package removeEntity = Package.of("foo", null, null);
        packageRepository.save(removeEntity);
        long previousCount = packageRepository.count();

        packageRepository.deleteById(removeEntity.getPackageSeq());

        assertThat(packageRepository.count()).isEqualTo(previousCount - 1);
        assertThat(packageRepository.existsById(removeEntity.getPackageSeq())).isFalse();
    }

}