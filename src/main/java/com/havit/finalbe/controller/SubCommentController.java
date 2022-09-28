package com.havit.finalbe.controller;

import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.SubCommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/subcomment")
public class SubCommentController {

    private final SubCommentService subCommentService;

    @Operation(summary = "대댓글 생성", description = "대댓글이 생성 됩니다.")
    @PostMapping("/")
    public ResponseDto<?> createSubComment(@RequestBody SubCommentDto.Request subCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return subCommentService.createSubComment(subCommentRequestDto, userDetails);
    }

    @Operation(summary = "대댓글 수정", description = "subCommentId에 해당하는 대댓글을 수정합니다.")
    @PutMapping("/{subCommentId}")
    public ResponseDto<?> updateSubComment(@PathVariable Long subCommentId, @RequestBody SubCommentDto.Request subCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return subCommentService.updateSubComment(subCommentId, subCommentRequestDto, userDetails);
    }

    @Operation(summary = "대댓글 삭제", description = "subCommentId에 해당하는 대댓글을 삭제합니다.")
    @DeleteMapping("/{subCommentId}")
    public ResponseDto<?> deleteSubComment(@PathVariable Long subCommentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return subCommentService.deleteSubComment(subCommentId, userDetails);
    }
}
