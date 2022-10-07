package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "인증샷 요청 DTO")
public class CertifyRequestDto {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @NotBlank(message = "제목은 빈 칸일 수 없습니다.")
    @Schema(description = "제목", example = "6시 기상 완료")
    private String title;

    @NotNull(message = "이미지는 빈 칸일 수 없습니다.")
    @Schema(description = "이미지 파일", example = "1")
    private Long imageId;

    @Schema(description = "경도", example = "83.24982")
    private String longitude;

    @Schema(description = "위도", example = "92.34972")
    private String latitude;
}
