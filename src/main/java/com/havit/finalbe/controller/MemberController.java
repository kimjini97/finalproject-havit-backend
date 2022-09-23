package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.LoginRequestDto;
import com.havit.finalbe.dto.request.SignupRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "[회원 API]")
@RestController
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력하고 회원가입을 완료합니다")
    @PostMapping("/api/signup")
    public ResponseDto<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.sigunup(signupRequestDto);
    }

    @Operation(summary = "로그인", description = "회원 정보 시 기입한 이메일과 비밀번호로 로그인을 합니다.(Access Token, Refresh Token 생성)")
    @PostMapping("/api/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return memberService.login(loginRequestDto, response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.(Refresh Token 파기")
    @RequestMapping(value = "/api/auth/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.logout(request, userDetails);
    }
}
