package com.havit.finalbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "비밀번호 확인 요청 DTO")
public class CheckPasswordRequestDto {

    @Schema(description = "비밀번호", example = "test123")
    private String password;
}
