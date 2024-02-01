package com.example.ptbatch.repository.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author daecheol song
 * @since 1.0
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_group")
public class UserGroup {

    @Id
    private String userGroupId;
    @Setter
    private String userGroupName;
    @Setter
    private String description;

    private UserGroup(String userGroupId, String userGroupName, String description) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.description = description;
    }

    public static UserGroup of(String userGroupId, String userGroupName, String description) {
        return new UserGroup(userGroupId, userGroupName, description);
    }
}
