package com.havit.finalbe.controller;

import com.havit.finalbe.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = {"이미지 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/image")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "이미지를 S3에 업로드 후, imageId 값을 반환합니다.")
    @PostMapping("/")
    public Long getImageId(@RequestPart(value = "image") MultipartFile multipartFile) throws IOException {
        return imageService.getImageId(multipartFile, "havit");
    }

    @Operation(summary = "이미지 삭제", description = "id값에 해당하는 이미지를 삭제합니다.")
    @DeleteMapping("/{imageId}")
    public String deleteImage(@PathVariable Long imageId) {
        return imageService.deleteImage(imageId);
    }

    @Operation(summary = "랜덤 이미지 업로드", description = "랜덤 프로필로 쓸 이미지를 첨부합니다.")
    @PostMapping("/random")
    public Long randomImgUpload(@RequestPart(value = "image") MultipartFile multipartFile) throws IOException {
        return imageService.randomImgUpload(multipartFile, "random");
    }

    @Operation(summary = "랜덤 이미지 id", description = "프로필로 쓸 이미지 id를 랜덤으로 불러옵니다.")
    @GetMapping("/random")
    public int getRandomProfileId() {
        return imageService.randomImage();
    }
}
