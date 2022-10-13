package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver_MemberId(Long memberId);
}
