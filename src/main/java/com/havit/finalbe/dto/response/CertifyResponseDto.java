package com.havit.finalbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertifyResponseDto {
    private Long certifyId;
    private Long groupId;
    private String title;
    private String imgUrl;
    private double longitude;
    private double latitude;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;
    private String leaderName;
    private String crewName;
    private String profileUrl;
    private List<CommentResponseDto> commentList;
}
