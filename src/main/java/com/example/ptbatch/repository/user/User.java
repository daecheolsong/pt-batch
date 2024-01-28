package com.example.ptbatch.repository.user;

import com.example.ptbatch.repository.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    private String userId;
    @Setter
    private String userName;
    @Setter
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Setter
    private String phone;
    @Setter
    private String meta;

    private User(String userId, String userName, UserStatus status, String phone, String meta) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

    public static User of(String userId, String userName, UserStatus status, String phone, String meta) {
        return new User(userId, userName, status, phone, meta);
    }
}
