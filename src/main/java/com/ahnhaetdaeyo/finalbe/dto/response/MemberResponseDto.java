package com.ahnhaetdaeyo.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "멤버 ResponseDto")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    @Schema(description = "멤버 고유 id(PK)")
    private Long id;
    
    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;
    
    @Schema(description = "닉네임", example = "루피짱")
    private String nickname;
}
