package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.LoginRequestDto;
import com.havit.finalbe.dto.request.SignupRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MemberService;
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

    @PostMapping("/api/signup")
    public ResponseDto<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.sigunup(signupRequestDto);
    }

    @PostMapping("/api/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return memberService.login(loginRequestDto, response);
    }

    @RequestMapping(value = "/api/auth/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.logout(request, userDetails);
    }
}
