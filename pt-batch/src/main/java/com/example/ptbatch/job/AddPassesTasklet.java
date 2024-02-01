package com.example.ptbatch.job;

import com.example.ptbatch.repository.pass.*;
import com.example.ptbatch.repository.user.UserGroupMapping;
import com.example.ptbatch.repository.user.UserGroupMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author daecheol song
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AddPassesTasklet implements Tasklet {
    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        List<BulkPass> entities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt)
                .stream().toList();

        List<Pass> passEntities = entities.stream()
                .flatMap(entity -> userGroupMappingRepository.findByUserGroupId(entity.getUserGroupId()).stream())
                .map(UserGroupMapping::getUserId)
                .flatMap(userId -> entities.stream()
                            .map(ent -> Pass.of(ent.getPackageSeq(), userId, PassStatus.READY,
                                    ent.getCount(), ent.getStartedAt(), ent.getEndedAt(), null))
                )
                .toList();

        List<Pass> savedPasses = passRepository.saveAll(passEntities);
        entities.forEach(afterExecutedEntities -> afterExecutedEntities.setStatus(BulkPassStatus.COMPLETED));
        log.info("AddPasses Tasklet - execute : 이용권 {} 추가 완료, startedAt = {}", savedPasses.size(), startedAt);

        return RepeatStatus.FINISHED;
    }

}
