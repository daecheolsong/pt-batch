package com.example.ptbatch.job;

import com.example.ptbatch.repository.pass.*;
import com.example.ptbatch.repository.user.UserGroupMapping;
import com.example.ptbatch.repository.user.UserGroupMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
public class AddPassesTaskletTest {
    @Mock
    private StepContribution stepContribution;
    @Mock
    private ChunkContext chunkContext;
    @Mock
    private PassRepository passRepository;
    @Mock
    private BulkPassRepository bulkPassRepository;
    @Mock
    private UserGroupMappingRepository userGroupMappingRepository;

    @InjectMocks
    private AddPassesTasklet addPassesTasklet;


    @Test
    public void test_execute() throws Exception{
        final String userGroupId = "GROUP";
        final String userId = "A1000000";
        final Integer packageSeq = 1;
        final Integer count = 10;
        final LocalDateTime now = LocalDateTime.now();
        final BulkPass bulkPassEntity = BulkPass.of(packageSeq, userGroupId, BulkPassStatus.READY, 10, now, now.plusDays(60));
        final UserGroupMapping userGroupMappingEntity = UserGroupMapping.of(userGroupId, userId);
        when(bulkPassRepository.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any())).thenReturn(List.of(bulkPassEntity));
        when(userGroupMappingRepository.findByUserGroupId(eq("GROUP"))).thenReturn(List.of(userGroupMappingEntity));
        when(passRepository.saveAll(anyList()))
                .thenReturn(List.of(Pass.of(bulkPassEntity.getPackageSeq(), userId, PassStatus.READY, bulkPassEntity.getCount(), bulkPassEntity.getStartedAt(), bulkPassEntity.getEndedAt(), null)));

        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);


        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
        ArgumentCaptor<List> passEntitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(passRepository, times(1)).saveAll(passEntitiesCaptor.capture());
        final List<Pass> passEntities = passEntitiesCaptor.getValue();
        assertThat(passEntities.size()).isEqualTo(1);
        assertThat(passEntities)
                .allSatisfy(pass -> {
                    assertThat(pass.getPackageSeq()).isEqualTo(1);
                    assertThat(pass.getUserId()).isEqualTo(userId);
                    assertThat(pass.getStatus()).isEqualTo(PassStatus.READY);
                    assertThat(pass.getRemainingCount()).isEqualTo(count);
                });

    }

}