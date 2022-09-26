package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.SubCommentRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.SubCommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class SubCommentController {

    private final SubCommentService subCommentService;

    @Operation(summary = "대댓글 생성", description = "대댓글이 생성 됩니다.")
    @PostMapping("/api/auth/subcomment")
    public ResponseDto<?> createSubComment(@RequestBody SubCommentRequestDto subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.createSubComment(subCommentRequestDto, request);
    }

    @Operation(summary = "대댓글 수정", description = "subCommentId에 해당하는 대댓글을 수정합니다.")
    @PutMapping("/api/auth/subcomment/{subCommentId}")
    public ResponseDto<?> updateSubComment(@PathVariable Long subCommentId, @RequestBody SubCommentRequestDto subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.updateSubComment(subCommentId, subCommentRequestDto, request);
    }

    @Operation(summary = "대댓글 삭제", description = "subCommentId에 해당하는 대댓글을 삭제합니다.")
    @DeleteMapping("/api/auth/subcomment/{subCommentId}")
    public ResponseDto<?> deleteSubComment(@PathVariable Long subCommentId, HttpServletRequest request) {
        return subCommentService.deleteSubComment(subCommentId, request);
    }
}
