package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "인증샷 응답 DTO")
public class CertifyResponseDto {

    @Schema(description = "인증 id", example = "1")
    private Long certifyId;

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "인증 제목", example = "6시 기상 완료")
    private String title;

    @Schema(description = "인증 이미지", example = "1")
    private Long imageId;

    @Schema(description = "경도", example = "83.23423")
    private String longitude;

    @Schema(description = "위도", example = "90.23434")
    private String latitude;

    @Schema(description = "생성 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime modifiedAt;

    @Schema(description = "작성자 PK", example = "1")
    private Long memberId;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "리더명", example = "추장")
    private String leaderName;

    @Schema(description = "크루원명", example = "백성")
    private String crewName;

    @Schema(description = "프로필 이미지", example = "1")
    private Long profileImageId;

    @Schema(description = "댓글 목록")
    private List<CommentResponseDto> commentList;
}
