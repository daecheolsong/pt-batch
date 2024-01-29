package com.example.ptbatch.repository.notification;

import com.example.ptbatch.repository.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationSeq;
    @Setter
    private String uuid;
    @Enumerated(EnumType.STRING)
    @Setter
    private NotificationEvent event;
    @Setter
    private String text;
    @Setter
    private boolean sent;
    @Setter
    private LocalDateTime sentAt;


    private Notification(String uuid, NotificationEvent event, String text, boolean sent, LocalDateTime sentAt) {
        this.uuid = uuid;
        this.event = event;
        this.text = text;
        this.sent = sent;
        this.sentAt = sentAt;
    }


    public static Notification of(String uuid, NotificationEvent event, String text, boolean sent, LocalDateTime sentAt) {
        return new Notification(uuid, event, text, sent, sentAt);
    }
}