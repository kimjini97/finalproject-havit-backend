package com.ahnhaetdaeyo.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 RequestDto")
@Getter
@Builder
@AllArgsConstructor
public class LoginRequestDto {
    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;
    @Schema(description = "비밀번호", example = "test123")
    private String password;
}
