package com.havit.finalbe.controller;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.ParticipateService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"참가자 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/participate")
public class ParticipateController {

    private final ParticipateService participateService;

    @Operation(summary = "그룹 참여하기", description = "해당하는 그룹의 참여 이력을 확인하고 참여합니다.")
    @PostMapping("/{groupId}")
    public GroupDto.Response participate(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return participateService.participate(groupId, userDetails);
    }

    @Operation(summary = "그룹 참여 취소하기", description = "해당하는 그룹의 참여 이력을 확인하고 취소합니다.")
    @DeleteMapping("/{groupId}")
    public GroupDto.Response cancelParticipation(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return participateService.cancelParticipation(groupId, userDetails);
    }
}
