package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "댓글 요청 DTO")
public class CommentRequestDto {

    @Schema(description = "인증샷 id", example = "1")
    private Long certifyId;

    @Schema(description = "댓글 내용", example = "오늘도 6시에 일어나느라 수고하셨습니다!")
    private String content;
}
