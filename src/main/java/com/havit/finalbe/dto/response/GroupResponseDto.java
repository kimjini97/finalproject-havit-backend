package com.havit.finalbe.dto.response;

import com.havit.finalbe.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 응답 DTO")
public class GroupResponseDto {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "그룹명", example = "아침 6시 기상러들")
    private String title;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "리더명", example = "추장")
    private String leaderName;

    @Schema(description = "크루원명", example = "백성")
    private String crewName;

    @Schema(description = "개시일", example = "2022-07-25T12:43:01.226062")
    private String startDate;

    @Schema(description = "그룹 내용", example = "아침 6시에 기상하는 습관을 기르고 싶은 사람들의 모임입니다!")
    private String content;

    @Schema(description = "그룹 이미지", example = "1")
    private Long imageId;

    @Schema(description = "작성 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime modifiedAt;

    @Schema(description = "그룹 태그")
    private List<String> groupTag;

    @Schema(description = "멤버 수", example = "15")
    private int memberCount;

    @Schema(description = "멤버 목록")
    private List<Member> memberList;

    @Schema(description = "인증 이미지 Url 목록")
    private List<CertifyResponseDto> certifyList;
}
