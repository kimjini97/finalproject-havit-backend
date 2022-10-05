package com.havit.finalbe.dto.response;

import com.havit.finalbe.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지를 하지 않은 멤버 목록 응답 DTO")
public class UnchallengerResponseDto {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "멤버 정보")
    private Member member;
}
