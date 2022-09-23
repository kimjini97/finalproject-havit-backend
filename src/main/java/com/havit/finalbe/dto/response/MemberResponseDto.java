package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "멤버 응답Dto")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    @Schema(description = "멤버 id", example = "1")
    private Long memberId;
    
    @Schema(description = "이메일", example = "test@gmail.com")
    private String username;
    
    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "가입 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
    private LocalDateTime modifiedAt;
}
