package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupController {

    @PostMapping("/api/auth/group")
    public ResponseDto<?> createGroup() {
        return null;
    }

    @GetMapping("/api/auth/group")
    public ResponseDto<?> getAllGroup() {
        return null;
    }

    @GetMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> getGroupDetail(@PathVariable Long groupId) {
        return null;
    }

    @PutMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> updateGroup(@PathVariable Long groupId) {
        return null;
    }

    @DeleteMapping("/api/auth/group/{groupId}")
    public ResponseDto<?> deleteGroup(@PathVariable Long groupId) {
        return null;
    }
}
