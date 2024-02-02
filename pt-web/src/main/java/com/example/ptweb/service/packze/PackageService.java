package com.example.ptweb.service.packze;

import com.example.ptbatch.repository.packze.Package;
import com.example.ptbatch.repository.packze.PackageRepository;
import com.example.ptweb.dto.PackageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;

    public List<PackageDto> getAllPackages() {
        return packageRepository
                .findAllByOrderByPackageName()
                .stream().map(PackageDto::from)
                .toList();
    }
}
