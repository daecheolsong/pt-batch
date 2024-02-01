package com.example.ptbatch.repository.user;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroupMappingId implements Serializable {
    private String userGroupId;
    private String userId;
    private UserGroupMappingId(String userGroupId, String userId) {
        this.userGroupId = userGroupId;
        this.userId = userId;
    }

    public static UserGroupMappingId of(String userGroupId, String userId) {
        return new UserGroupMappingId(userGroupId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGroupMappingId that)) return false;
        return Objects.equals(getUserGroupId(), that.getUserGroupId()) && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserGroupId(), getUserId());
    }

}
