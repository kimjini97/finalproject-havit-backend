package com.havit.finalbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Schema(description = "멤버 DTO")
public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Signup {

        @Schema(description = "이메일", example = "test@gmail.com")
        private String email;

        @Schema(description = "비빌번호", example = "test123")
        private String password;

        @Schema(description = "비밀번호 확인", example = "test123")
        private String passwordConfirm;

        @Schema(description = "닉네임", example = "루피짱")
        private String nickname;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {

        @Schema(description = "이메일", example = "test@gmail.com")
        private String email;

        @Schema(description = "비밀번호", example = "test123")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckPassword {

        @Schema(description = "비밀번호", example = "test123")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyPage {

        @Schema(description = "프로필 이미지", example = "1")
        private Long imageId;

        @Schema(description = "닉네임", example = "지니짱")
        private String nickname;

        @Schema(description = "소개", example = "지니는 짱짱맨")
        private String introduce;

        @Schema(description = "비빌번호", example = "test123")
        private String password;

        @Schema(description = "비밀번호 확인", example = "test123")
        private String passwordConfirm;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "멤버 id", example = "1")
        private Long memberId;

        @Schema(description = "이메일", example = "test@gmail.com")
        private String username;

        @Schema(description = "닉네임", example = "김병처리")
        private String nickname;

        @Schema(description = "프로필 이미지 url", example = "1")
        private Long imageId;

        @Schema(description = "소개", example = "지니는 짱짱맨")
        private String introduce;

        @Schema(description = "가입 일시", example = "2022-07-25T12:43:01.226062")
        private LocalDateTime createdAt;

        @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
        private LocalDateTime modifiedAt;
    }
}
