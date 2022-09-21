package com.havit.finalbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCommentResponseDto {
    private Long subCommentId;
    private Long commentId;
    private String nickname;
    private String profileUrl;
    private String content;
    private String dateTime;
}
