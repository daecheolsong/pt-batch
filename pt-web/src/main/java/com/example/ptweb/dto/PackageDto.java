package com.example.ptweb.dto;


import com.example.ptbatch.repository.packze.Package;

/**
 * @author daecheol song
 * @since 1.0
 */
public record PackageDto(Integer packageSeq, String packageName) {

    public static PackageDto from(Package entity) {
        return new PackageDto(
                entity.getPackageSeq(),
                entity.getPackageName(
                )
        );
    }
}
