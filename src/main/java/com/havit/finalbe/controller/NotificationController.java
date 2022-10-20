package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.NotificationRequestDto;
import com.havit.finalbe.dto.response.NotificationsResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    // 로그인한 유저 SSE 연결
//    @GetMapping(value = "/api/auth/subscribe", produces = "text/event-stream")
//    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return notificationService.subscribe(userDetails, lastEventId);
//    }

    @GetMapping(value = "/subscribe/{memberId}", produces = "text/event-stream;charset=utf-8")
    public SseEmitter subscribe(@PathVariable Long memberId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(memberId, lastEventId);
    }


    // 알림 보내기
    @PostMapping("/api/auth/notification")
    public String sendNotification(@RequestBody NotificationRequestDto request) {
        return notificationService.send(request.getMemberId(), request.getGroupId());
    }

    // 로그인한 유저의 모든 알림 조회
    @GetMapping("/api/auth/notification")
    public NotificationsResponseDto getMyNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.findAllByMemberId(userDetails);
    }

    // 알림 읽음 상태 변경
    @PatchMapping("/api/auth/notification/{notificationId}")
    public boolean isReadNotification(@PathVariable Long notificationId) {
        return notificationService.isReadNotification(notificationId);
    }

    // 해당 알림 DB 에서 삭제
    @DeleteMapping("/api/auth/notification/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
}
