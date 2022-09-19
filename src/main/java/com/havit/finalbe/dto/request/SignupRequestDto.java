package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원가입 RequestDto")
@Getter
@Builder
@AllArgsConstructor
public class SignupRequestDto {

    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;

    @Schema(description = "비빌번호", example = "test123")
    private String password;

    @Schema(description = "비밀번호 확인", example = "test123")
    private String passwordConfirm;

    @Schema(description = "닉네임", example = "루피짱")
    private String nickname;
}
