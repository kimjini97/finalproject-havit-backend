package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "비밀번호 변경 요청 DTO")
public class MyPasswordRequestDto {

    @Schema(description = "비빌번호", example = "test123")
    private String password;

    @Schema(description = "비밀번호 확인", example = "test123")
    private String passwordConfirm;
}
