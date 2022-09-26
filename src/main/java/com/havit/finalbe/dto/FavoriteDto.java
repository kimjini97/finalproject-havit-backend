package com.havit.finalbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "즐겨찾기 DTO")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;
}
