package com.example.ptbatch.repository.user;

import com.example.ptbatch.repository.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserGroupMappingId.class)
@Table(name = "user_group_mapping")
public class UserGroupMapping extends BaseEntity {

    @Id
    private String userGroupId;
    @Id
    private String userId;

    private UserGroupMapping(String userGroupId, String userId) {
        this.userGroupId = userGroupId;
        this.userId = userId;
    }

    public static UserGroupMapping of(String userGroupId, String userId) {
        return new UserGroupMapping(userGroupId, userId);
    }
}
