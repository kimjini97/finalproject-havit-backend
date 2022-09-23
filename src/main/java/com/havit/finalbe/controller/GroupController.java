package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[그룹 API]")
@RestController
@RequiredArgsConstructor
public class GroupController {

    @Operation(summary = "그룹 생성", description = "그룹 관련 정보 기입후 그룹이 생성 됩니다.")
    @PostMapping("/api/auth/group")
    public ResponseDto<?> createGroup() {
        return null;
    }

    @Operation(summary = "전체 그룹 조회", description = "생성된 전체 그룹을 조회합니다.")
    @GetMapping("/api/auth/group")
    public ResponseDto<?> getAllGroup() {
        return null;
    }

    @Operation(summary = "그룹 상세 조회", description = "groupId에 해당하는 그룹을 조회합니다.")
    @GetMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> getGroupDetail(@PathVariable Long groupId) {
        return null;
    }

    @Operation(summary = "그룹 수정", description = "groupId에 해당하는 그룹을 수정합니다.")
    @PutMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> updateGroup(@PathVariable Long groupId) {
        return null;
    }

    @Operation(summary = "그룹 삭제", description = "groupId에 해당하는 그룹을 삭제합니다.")
    @DeleteMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> deleteGroup(@PathVariable Long groupId) {
        return null;
    }
}
