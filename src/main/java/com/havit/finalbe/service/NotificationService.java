package com.havit.finalbe.service;

import com.havit.finalbe.dto.response.NotificationResponseDto;
import com.havit.finalbe.dto.response.NotificationsResponseDto;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Notification;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.EmitterRepository;
import com.havit.finalbe.repository.GroupRepository;
import com.havit.finalbe.repository.MemberRepository;
import com.havit.finalbe.repository.NotificationRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;

    public SseEmitter subscribe(UserDetailsImpl userDetails, String lastEventId) {
        Long memberId = userDetails.getMember().getMemberId();
        String emitterId = memberId + "_" + System.currentTimeMillis();

        // 생성된 emitterId 를 기반으로 emitter 를 저장
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // emitter 의 시간이 만료된 후 레포에서 삭제
        emitter.onCompletion(() -> emitterRepository.deleteByEmitterId(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteByEmitterId(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, emitterId, "EventStream Created. [memberId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteByEmitterId(emitterId);
        }
    }

    @Transactional
    public String send(Long receiverId, Long groupId) {
        Member receiver = memberRepository.findMemberByMemberId(receiverId);
        Groups groups = groupRepository.findByGroupId(groupId);
        String content = "[" + groups.getTitle() + "] 챌린지에 참여해주세요!";

        Notification notification = Notification.builder()
                                        .receiver(receiver)
                                        .groups(groups)
                                        .content(content)
                                        .groupUrl("/api/auth/group/" + groups.getGroupId())
                                        .isRead(false)
                                        .build();
        notificationRepository.save(notification);

        String memberId = String.valueOf(receiver.getMemberId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmittersStartWithByMemberId(memberId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponseDto.from(notification));
                }
        );

        return "success";
    }

    @Transactional
    public NotificationsResponseDto findAllByMemberId(UserDetailsImpl userDetails) {
        List<NotificationResponseDto> notificationResponseDtoList =
                notificationRepository.findAllByReceiver_MemberIdOrderByCreatedAtDesc(userDetails.getMember().getMemberId()).stream()
                        .map(NotificationResponseDto::from)
                        .collect(Collectors.toList());

        Long unreadCount = notificationResponseDtoList.stream()
                .filter(notification -> !notification.isRead())
                .count();

        return NotificationsResponseDto.of(notificationResponseDtoList, unreadCount);
    }

    @Transactional
    public boolean isReadNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.read();
        return notification.isRead();
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
