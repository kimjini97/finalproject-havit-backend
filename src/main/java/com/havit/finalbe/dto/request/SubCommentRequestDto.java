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
@Schema(description = "대댓글 요청 DTO")
public class SubCommentRequestDto {

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "대댓글 내용", example = "칭찬 감사요~")
    private String content;
}
