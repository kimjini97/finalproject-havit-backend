package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "멤버별 프로필 응답 DTO")
public class MemberProfileResponseDto {

    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "이메일", example = "test@gmail.com")
    private String username;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "프로필 이미지 url", example = "1")
    private Long imageId;

    @Schema(description = "소개", example = "지니는 짱짱맨")
    private String introduce;

    @Schema(description = "가입 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime modifiedAt;

    @Schema(description = "해당 멤버가 참여한 그룹 목록")
    private List<AllGroupListResponseDto> groupList;

    @Schema(description = "해당 멤버가 작성한 인증샷 목록")
    private List<CertifyResponseDto> certifyList;
}
