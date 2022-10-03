package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원정보 수정 요청 DTO")
public class MyPageRequestDto {

    @Schema(description = "프로필 이미지", example = "1")
    private Long imageId;

    @Schema(description = "닉네임", example = "지니짱")
    private String nickname;

    @Schema(description = "소개", example = "지니는 짱짱맨")
    private String introduce;
}
