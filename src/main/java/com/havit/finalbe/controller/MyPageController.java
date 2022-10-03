package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.CheckPasswordRequestDto;
import com.havit.finalbe.dto.request.MyPageRequestDto;
import com.havit.finalbe.dto.request.MyPasswordRequestDto;
import com.havit.finalbe.dto.response.AllGroupListResponseDto;
import com.havit.finalbe.dto.response.MemberResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"마이페이지 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "마이 페이지 비밀번호 확인", description = "마이 페이지에 들어가기 전 비밀번호를 확인합니다.")
    @PostMapping("/check")
    public String checkPassword(@RequestBody CheckPasswordRequestDto checkPasswordDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.checkPassword(checkPasswordDto, userDetails);
    }

    @Operation(summary = "내가 참여한 그룹 목록", description = "내가 참여한 그룹 목록을 불러옵니다.")
    @GetMapping("/group")
    public List<AllGroupListResponseDto> getMyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyGroup(userDetails);
    }

    @Operation(summary = "내 정보 수정", description = "프로필 사진, 닉네임, 소개를 수정합니다.")
    @PutMapping(value = "/")
    public MemberResponseDto editMyInfo(@RequestBody MyPageRequestDto myPageDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.editMyInfo(myPageDto, userDetails);
    }

    @Operation(summary = "내 비밀번호 수정", description = "비밀번호를 수정합니다.")
    @PutMapping(value = "/private")
    public MemberResponseDto editMyPass(@RequestBody MyPasswordRequestDto myPassDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.editMyPassword(myPassDto, userDetails);
    }

    @Operation(summary = "내 프로필 이미지 삭제", description = "프로필 사진을 삭제합니다.")
    @PutMapping("/image")
    public MemberResponseDto deleteProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteProfile(userDetails);
    }
}
