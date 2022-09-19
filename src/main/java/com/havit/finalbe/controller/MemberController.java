package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.LoginRequestDto;
import com.havit.finalbe.dto.request.SignupRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[회원 API]")
@RestController
@AllArgsConstructor
public class MemberController {

    @PostMapping("/api/signup")
    public ResponseDto<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return null;
    }

    @PostMapping("/api/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return null;
    }
}
