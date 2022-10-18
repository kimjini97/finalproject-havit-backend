package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Groups groups;

    private String content;

    private String groupUrl;

    private boolean isRead;

    public void read() {
        this.isRead = true;
    }
}
