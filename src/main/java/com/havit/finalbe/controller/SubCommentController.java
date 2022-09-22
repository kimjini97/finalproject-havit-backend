package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.SubCommentRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.SubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class SubCommentController {

    private final SubCommentService subCommentService;

    @PostMapping("/api/auth/subcomment")
    public ResponseDto<?> createSubComment(@RequestBody SubCommentRequestDto subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.createSubComment(subCommentRequestDto, request);
    }

    @PutMapping("/api/auth/subcomment/{subCommentId}")
    public ResponseDto<?> updateSubComment(@PathVariable Long subCommentId, @RequestBody SubCommentRequestDto subCommentRequestDto,
                                           HttpServletRequest request) {
        return subCommentService.updateSubComment(subCommentId, subCommentRequestDto, request);
    }

    @DeleteMapping("/api/auth/subcomment/{subCommentId}")
    public ResponseDto<?> deleteSubComment(@PathVariable Long subCommentId, HttpServletRequest request) {
        return subCommentService.deleteSubComment(subCommentId, request);
    }
}
