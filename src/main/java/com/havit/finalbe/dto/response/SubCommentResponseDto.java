package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대댓글 응답 DTO")
public class SubCommentResponseDto {

    @Schema(description = "대댓글 id", example = "1")
    private Long subCommentId;

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "작성자 PK", example = "1")
    private Long memberId;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "프로필 이미지 Url", example = "1")
    private Long profileImageId;

    @Schema(description = "대댓글 내용", example = "댓글 달아주셔서 감사합니다.")
    private String content;

    @Schema(description = "작성 일시", example = "2022-07-25T12:43:01.226062")
    private String dateTime;
}
