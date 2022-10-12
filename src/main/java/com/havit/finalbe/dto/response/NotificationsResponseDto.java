package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsResponseDto {

    @Schema(description = "로그인한 유저의 모든 알림")
    private List<NotificationResponseDto> notificationList;

    @Schema(description = "로그인한 유저가 읽지 않은 알림 수", example = "7")
    private Long unreadCount;

    public static NotificationsResponseDto of(List<NotificationResponseDto> notificationResponseDtoList, Long count) {
        return NotificationsResponseDto.builder()
                .notificationList(notificationResponseDtoList)
                .unreadCount(count)
                .build();
    }
}
