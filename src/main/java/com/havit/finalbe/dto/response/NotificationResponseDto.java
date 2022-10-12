package com.havit.finalbe.dto.response;

import com.havit.finalbe.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    @Schema(description = "알림 id", example = "1")
    private Long notificationId;

    @Schema(description = "알림 내용", example = "챌린지에 참여해주세요!")
    private String content;

    @Schema(description = "알림 클릭 시 이동할 groupUrl")
    private String groupUrl;

    @Schema(description = "알림이 생성된 날짜", example = "2022-10-12T16:58:52.453194")
    private LocalDateTime createdAt;

    @Schema(description = "알림 읽음 여부", example = "false")
    private boolean read;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .content(notification.getContent())
                .groupUrl(notification.getGroupUrl())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}
