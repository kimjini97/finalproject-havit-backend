package com.havit.finalbe.controller;

import com.havit.finalbe.dto.MemberDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = {"마이페이지 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "마이 페이지 비밀번호 확인", description = "마이 페이지에 들어가기 전 비밀번호를 확인합니다.")
    @PostMapping("/check")
    public ResponseDto<String> checkPassword(@RequestBody MemberDto.CheckPassword checkPasswordDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.checkPassword(checkPasswordDto, userDetails);
    }

    @Operation(summary = "내 정보 수정", description = "프로필 사진, 닉네임, 비밀번호를 수정합니다.")
    @PutMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseDto<MemberDto.Response> editMyInfo(@ModelAttribute MemberDto.MyPage myPageDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return myPageService.editMyInfo(myPageDto, userDetails);
    }

    @Operation(summary = "내 프로필 이미지 삭제", description = "프로필 사진을 삭제합니다.")
    @PutMapping("/image")
    public ResponseDto<MemberDto.Response> deleteProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteProfile(userDetails);
    }
}
