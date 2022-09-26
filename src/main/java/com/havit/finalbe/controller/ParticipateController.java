package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.ParticipateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ParticipateController {

    private final ParticipateService participateService;

    @Operation(summary = "그룹 참여하기", description = "해당하는 그룹의 참여 이력을 확인하고 참여합니다.")
    @PostMapping("/api/auth/participate/{groupId}")
    public ResponseDto<?> participate(@PathVariable Long groupId, HttpServletRequest request) {
        return participateService.participate(groupId, request);
    }

    @Operation(summary = "그룹 참여 취소하기", description = "해당하는 그룹의 참여 이력을 확인하고 취소합니다.")
    @DeleteMapping("/api/auth/participate/{groupId}")
    public ResponseDto<?> cancelParticipation(@PathVariable Long groupId, HttpServletRequest request) {
        return participateService.cancelParticipation(groupId, request);
    }
}
