package com.havit.finalbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "댓글 DTO")
public class CommentDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @Schema(description = "인증샷 id", example = "1")
        private Long certifyId;

        @Schema(description = "댓글 내용", example = "오늘도 6시에 일어나느라 수고하셨습니다!")
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

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
        private List<SubCommentDto.Response> subCommentList;
    }
}
