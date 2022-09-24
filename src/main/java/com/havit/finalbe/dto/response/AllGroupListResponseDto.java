package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "그룹 전체 목록 응답DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllGroupListResponseDto {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "그룹명", example = "아침 6시 기상러들")
    private String title;

    @Schema(description = "그룹 이미지", example = "aws 이미지 Url")
    private String imgUrl;

    @Schema(description = "멤버 수", example = "15")
    private int memberCount;

    @Schema(description = "그룹 태그")
    private List<String> groupTag;

    @Schema(description = "즐겨찾기", example = "true")
    private boolean favorite;

    @Schema(description = "작성 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime modifiedAt;
}
