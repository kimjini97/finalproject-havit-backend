package com.havit.finalbe.controller;

import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.SubCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "[대댓글 API]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/subcomment")
public class SubCommentController {

    private final SubCommentService subCommentService;

    @Operation(summary = "대댓글 생성", description = "대댓글이 생성 됩니다.")
    @PostMapping("/")
    public ResponseDto<?> createSubComment(@RequestBody SubCommentDto.Request subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.createSubComment(subCommentRequestDto, request);
    }

    @Operation(summary = "대댓글 수정", description = "subCommentId에 해당하는 대댓글을 수정합니다.")
    @PutMapping("/{subCommentId}")
    public ResponseDto<?> updateSubComment(@PathVariable Long subCommentId, @RequestBody SubCommentDto.Request subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.updateSubComment(subCommentId, subCommentRequestDto, request);
    }

    @Operation(summary = "대댓글 삭제", description = "subCommentId에 해당하는 대댓글을 삭제합니다.")
    @DeleteMapping("/{subCommentId}")
    public ResponseDto<?> deleteSubComment(@PathVariable Long subCommentId, HttpServletRequest request) {
        return subCommentService.deleteSubComment(subCommentId, request);
    }
}
