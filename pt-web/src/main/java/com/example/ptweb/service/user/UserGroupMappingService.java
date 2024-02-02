package com.example.ptweb.service.user;

import com.example.ptbatch.repository.user.UserGroupMappingRepository;
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
public class UserGroupMappingService {

    private final UserGroupMappingRepository userGroupMappingRepository;

    @Transactional(readOnly = true)
    public List<String> getAllUserGroupIds() {
        return userGroupMappingRepository.findDistinctUserGroupId();
    }
}
