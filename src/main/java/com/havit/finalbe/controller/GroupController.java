package com.havit.finalbe.controller;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.havit.finalbe.service.GroupService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Tag(name = "[그룹 API]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/group")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성", description = "그룹 관련 정보 기입후 그룹이 생성 됩니다.")
    @PostMapping("/")
    public ResponseDto<?> createGroup(@ModelAttribute GroupDto.Request groupRequestDto, HttpServletRequest request) throws IOException {
        return groupService.createGroup(groupRequestDto, request);
    }

    @Operation(summary = "전체 그룹 조회", description = "생성된 전체 그룹을 조회합니다.")
    @GetMapping("/")
    public ResponseDto<?> getAllGroup(HttpServletRequest request) {
        return groupService.getAllGroup(request);
    }

    @Operation(summary = "태그별 전체 그룹 조회", description = "생성된 전체 그룹을 태그별로 조회합니다.")
    @GetMapping("/tag")
    public ResponseDto<?> getAllGroup(HttpServletRequest request, @RequestParam(value = "tag") String keyword) {
        return groupService.getAllGroupByTag(request, keyword);
    }

    @Operation(summary = "그룹 상세 조회", description = "groupId에 해당하는 그룹을 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseDto<?> getGroupDetail(@PathVariable Long groupId, HttpServletRequest request) {
        return groupService.getGroupDetail(groupId, request);
    }

    @Operation(summary = "그룹 수정", description = "groupId에 해당하는 그룹을 수정합니다.")
    @PutMapping("/{groupId}")
    public ResponseDto<?> updateGroup(@PathVariable Long groupId,
                                      @ModelAttribute GroupDto.Request groupRequestDto, HttpServletRequest request) throws IOException {
        return groupService.updateGroup(groupId, groupRequestDto, request);
    }

    @Operation(summary = "그룹 삭제", description = "groupId에 해당하는 그룹을 삭제합니다.")
    @DeleteMapping("/{groupId}")
    public ResponseDto<?> deleteGroup(@PathVariable Long groupId, HttpServletRequest request) {
        return groupService.deleteGroup(groupId, request);
    }
}
