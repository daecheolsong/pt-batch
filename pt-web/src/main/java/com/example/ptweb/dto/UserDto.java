package com.example.ptweb.dto;

import com.example.ptbatch.repository.user.User;
import com.example.ptbatch.repository.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.thymeleaf.expression.Strings;
import org.thymeleaf.util.StringUtils;

/**
 * @author daecheol song
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public class UserDto {

    private String userId;
    private String userName;
    private UserStatus status;
    private String phone;
    private String meta;


    public static UserDto from(User entity) {
        return new UserDto(
                entity.getUserId(),
                entity.getUserName(),
                entity.getStatus(),
                entity.getPhone(),
                entity.getMeta() == null ? "" : entity.getMeta().toString()
        );
    }
}
