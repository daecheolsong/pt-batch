package com.example.ptweb.service.pass;

import com.example.ptbatch.repository.packze.Package;
import com.example.ptbatch.repository.packze.PackageRepository;
import com.example.ptbatch.repository.pass.BulkPass;
import com.example.ptbatch.repository.pass.BulkPassRepository;
import com.example.ptbatch.repository.pass.BulkPassStatus;
import com.example.ptweb.dto.BulkPassDto;
import com.example.ptweb.dto.request.BulkPassRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BulkPassService {

    private final BulkPassRepository bulkPassRepository;
    private final PackageRepository packageRepository;

    @Transactional(readOnly = true)
    public List<BulkPassDto> getAllBulkPasses() {
        return bulkPassRepository.findAllByOrderByStartedAtDesc()
                .stream()
                .map(BulkPassDto::from)
                .toList();
    }

    public BulkPassDto addBulkPass(BulkPassRequest bulkPassRequest) {
        Package packaze = packageRepository.findById(bulkPassRequest.getPackageSeq())
                .orElseThrow(EntityNotFoundException::new);

        BulkPass bulkPass = BulkPass.of(
                packaze.getPackageSeq(),
                bulkPassRequest.getUserGroupId(),
                BulkPassStatus.READY,
                packaze.getCount(),
                bulkPassRequest.getStartedAt(),
                bulkPassRequest.getStartedAt().plusDays(packaze.getPeriod())
        );

        return BulkPassDto.from(bulkPassRepository.save(bulkPass));
    }

}
