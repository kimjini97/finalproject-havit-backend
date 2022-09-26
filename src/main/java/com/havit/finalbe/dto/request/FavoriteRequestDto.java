package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "즐겨찾기 요청Dto")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequestDto {
    @Schema(description = "그룹 id", example = "1")
    private Long groupId;
}
