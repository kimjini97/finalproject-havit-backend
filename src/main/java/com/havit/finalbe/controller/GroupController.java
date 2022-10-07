package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.GroupRequestDto;
import com.havit.finalbe.dto.response.AllGroupListResponseDto;
import com.havit.finalbe.dto.response.GroupResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.havit.finalbe.service.GroupService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"그룹 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/group")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성", description = "그룹 관련 정보 기입후 그룹이 생성 됩니다.")
    @PostMapping(value = "/")
    public GroupResponseDto createGroup(@RequestBody @Valid GroupRequestDto groupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGroup(groupRequestDto, userDetails);
    }

    @Operation(summary = "전체 그룹 조회", description = "생성된 전체 그룹을 조회합니다.")
    @GetMapping("/")
    public List<AllGroupListResponseDto> getAllGroup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getAllGroup(userDetails);
    }

    @Operation(summary = "태그별 전체 그룹 조회", description = "생성된 전체 그룹을 태그별로 조회합니다.")
    @GetMapping("/tag")
    public List<AllGroupListResponseDto> getAllGroupByTag(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "tag") String keyword) {
        return groupService.getAllGroupByTag(userDetails, keyword);
    }

    @Operation(summary = "그룹 상세 조회", description = "groupId에 해당하는 그룹을 조회합니다.")
    @GetMapping("/{groupId}")
    public GroupResponseDto getGroupDetail(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.getGroupDetail(groupId, userDetails);
    }

    @Operation(summary = "그룹 수정", description = "groupId에 해당하는 그룹을 수정합니다.")
    @PatchMapping(value = "/{groupId}")
    public GroupResponseDto updateGroup(@PathVariable Long groupId,
                                         @RequestBody GroupRequestDto groupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.updateGroup(groupId, groupRequestDto, userDetails);
    }

    @Operation(summary = "그룹 삭제", description = "groupId에 해당하는 그룹을 삭제합니다.")
    @DeleteMapping("/{groupId}")
    public String deleteGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.deleteGroup(groupId, userDetails);
    }
}
