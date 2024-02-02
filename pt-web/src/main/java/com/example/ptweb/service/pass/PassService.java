package com.example.ptweb.service.pass;

import com.example.ptbatch.repository.pass.Pass;
import com.example.ptbatch.repository.pass.PassRepository;
import com.example.ptweb.dto.PassDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PassService {
    private final PassRepository passRepository;

    @Transactional(readOnly = true)
    public List<PassDto> getPasses(String userId) {
        return passRepository.findByUserId(userId)
                .stream().map(PassDto::from)
                .toList();
    }
}
