package com.example.ptbatch.repository.user;

import com.example.ptbatch.repository.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@TypeDef(name = "json", typeClass = JsonType.class)
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

    @Type(type = "json")
    @Setter
    private Map<String, Object> meta;

    public String getUUID() {
        return meta.containsKey("uuid") ? String.valueOf(meta.get("uuid")) : null;
    }

    private User(String userId, String userName, UserStatus status, String phone, Map<String, Object> meta) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

    public static User of(String userId, String userName, UserStatus status, String phone, Map<String, Object> meta) {
        return new User(userId, userName, status, phone, meta);
    }
}
