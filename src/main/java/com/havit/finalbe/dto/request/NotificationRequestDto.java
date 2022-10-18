package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "알림 보내기 요청 DTO")
public class NotificationRequestDto {

    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;
}
