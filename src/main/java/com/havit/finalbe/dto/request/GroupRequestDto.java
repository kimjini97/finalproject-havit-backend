package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 요청 DTO")
public class GroupRequestDto {

    @NotBlank
    @Schema(description = "그룹 명", example = "6시에 일어날 사람들~")
    private String title;

    @NotBlank
    @Schema(description = "리더명", example = "추장")
    private String leaderName;

    @NotBlank
    @Schema(description = "크루원명", example = "백성")
    private String crewName;

    @NotBlank
    @Schema(description = "그룹 내용", example = "아침 6시에 기상하는 습관을 기르고 싶은 사람들의 모임입니다!")
    private String content;

    @NotBlank
    @Schema(description = "그룹 태그", example = "#오늘기상, #2030")
    private List<String> groupTag;

    @NotBlank
    @Schema(description = "그룹 이미지", example = "1")
    private Long imageId;
}
