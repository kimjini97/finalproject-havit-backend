package com.havit.finalbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "인증샷 DTO")
public class CertifyDto {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "인증샷 요청 DTO")
    public static class Request {

        @Schema(description = "그룹 id", example = "1")
        private Long groupId;

        @Schema(description = "제목", example = "6시 기상 완료")
        private String title;

        @Schema(description = "이미지 파일", example = "기상.jpg")
        private MultipartFile imgFile;

        @Schema(description = "경도", example = "83.24982")
        private double longitude;

        @Schema(description = "위도", example = "92.34972")
        private double latitude;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "인증 id", example = "1")
        private Long certifyId;

        @Schema(description = "그룹 id", example = "1")
        private Long groupId;

        @Schema(description = "인증 제목", example = "6시 기상 완료")
        private String title;

        @Schema(description = "인증 이미지", example = "기상.jpg")
        private String imgUrl;

        @Schema(description = "경도", example = "83.23423")
        private double longitude;

        @Schema(description = "위도", example = "90.23434")
        private double latitude;

        @Schema(description = "생성 일시", example = "2022-07-25T12:43:01.226062")
        private LocalDateTime createdAt;

        @Schema(description = "수정 일시", example = "2022-07-25T12:43:01.226062")
        private LocalDateTime modifiedAt;

        @Schema(description = "닉네임", example = "김병처리")
        private String nickname;

        @Schema(description = "리더명", example = "추장")
        private String leaderName;

        @Schema(description = "크루원명", example = "백성")
        private String crewName;

        @Schema(description = "프로필 이미지", example = "aws 이미지 Url")
        private String profileUrl;

        @Schema(description = "댓글 목록")
        private List<CommentDto.Response> commentList;
    }
}
