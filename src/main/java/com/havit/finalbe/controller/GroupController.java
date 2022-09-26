package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.GroupRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.havit.finalbe.service.GroupService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성", description = "그룹 관련 정보 기입후 그룹이 생성 됩니다.")
    @PostMapping("/api/auth/group")
    public ResponseDto<?> createGroup(@ModelAttribute GroupRequestDto groupRequestDto, HttpServletRequest request) throws IOException {
        return groupService.createGroup(groupRequestDto, request);
    }

    @Operation(summary = "전체 그룹 조회", description = "생성된 전체 그룹을 조회합니다.")
    @GetMapping("/api/auth/group")
    public ResponseDto<?> getAllGroup(HttpServletRequest request) {
        return groupService.getAllGroup(request);
    }

    @Operation(summary = "태그별 전체 그룹 조회", description = "생성된 전체 그룹을 태그별로 조회합니다.")
    @GetMapping("/api/auth/group/tag")
    public ResponseDto<?> getAllGroup(HttpServletRequest request, @RequestParam(value = "tag") String keyword) {
        return groupService.getAllGroupByTag(request, keyword);
    }

    @Operation(summary = "그룹 상세 조회", description = "groupId에 해당하는 그룹을 조회합니다.")
    @GetMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> getGroupDetail(@PathVariable Long groupId, HttpServletRequest request) {
        return groupService.getGroupDetail(groupId, request);
    }

    @Operation(summary = "그룹 수정", description = "groupId에 해당하는 그룹을 수정합니다.")
    @PutMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> updateGroup(@PathVariable Long groupId,
                                      @ModelAttribute GroupRequestDto groupRequestDto, HttpServletRequest request) throws IOException {
        return groupService.updateGroup(groupId, groupRequestDto, request);
    }

    @Operation(summary = "그룹 삭제", description = "groupId에 해당하는 그룹을 삭제합니다.")
    @DeleteMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> deleteGroup(@PathVariable Long groupId, HttpServletRequest request) {
        return groupService.deleteGroup(groupId, request);
    }
}
