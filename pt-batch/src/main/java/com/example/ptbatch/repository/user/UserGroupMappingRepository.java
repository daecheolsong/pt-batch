package com.example.ptbatch.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, UserGroupMappingId> {
    List<UserGroupMapping> findByUserGroupId(String userGroupId);
}
