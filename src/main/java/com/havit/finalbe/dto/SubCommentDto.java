package com.havit.finalbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "대댓글 DTO")
public class SubCommentDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @Schema(description = "댓글 id", example = "1")
        private Long commentId;

        @Schema(description = "대댓글 내용", example = "칭찬 감사요~")
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "대댓글 id", example = "1")
        private Long subCommentId;

        @Schema(description = "댓글 id", example = "1")
        private Long commentId;

        @Schema(description = "닉네임", example = "김병처리")
        private String nickname;

        @Schema(description = "프로필 이미지 Url", example = "1")
        private Long profileImageId;

        @Schema(description = "대댓글 내용", example = "댓글 달아주셔서 감사합니다.")
        private String content;

        @Schema(description = "작성 일시", example = "2022-07-25T12:43:01.226062")
        private String dateTime;
    }
}
