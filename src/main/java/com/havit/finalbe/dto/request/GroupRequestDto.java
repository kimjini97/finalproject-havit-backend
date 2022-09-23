package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "그룹 요청Dto")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestDto {

    @Schema(description = "그룹 명", example = "6시에 일어날 사람들~")
    private String title;

    @Schema(description = "리더명", example = "추장")
    private String leaderName;

    @Schema(description = "크루원명", example = "백성")
    private String crewName;

    @Schema(description = "그룹 개시일", example = "9월 23일")
    private String startDate;

    @Schema(description = "그룹 내용", example = "아침 6시에 기상하는 습관을 기르고 싶은 사람들의 모임입니다!")
    private String content;

    @Schema(description = "그룹 태그", example = "#오늘기상, #2030")
    private List<String> groupTag;

    @Schema(description = "그룹 이미지", example = "기상.jpg")
    private MultipartFile imgFile;
}
