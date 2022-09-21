package com.havit.finalbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String nickname;
    private String profileUrl;
    private String content;
    private String dateTime;
    private List<SubCommentResponseDto> subCommentList;
}
