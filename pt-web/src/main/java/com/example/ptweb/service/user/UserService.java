package com.example.ptweb.service.user;

import com.example.ptbatch.repository.user.User;
import com.example.ptbatch.repository.user.UserRepository;
import com.example.ptweb.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto getUser(String userId) {
        return UserDto.from(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
    }

}
