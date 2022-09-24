package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "댓글 응답DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponseDto {

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "인증샷 id", example = "1")
    private Long certifyId;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "프로필 이미지", example = "aws 이미지 Url")
    private String profileUrl;

    @Schema(description = "댓글 내용", example = "오늘도 일어나느라 수고하셨습니다.")
    private String content;

    @Schema(description = "인증 id", example = "1")
    private String dateTime;

    @Schema(description = "대댓글 목록")
    private List<SubCommentResponseDto> subCommentList;
}
