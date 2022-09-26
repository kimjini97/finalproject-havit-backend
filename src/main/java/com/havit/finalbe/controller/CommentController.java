package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.CommentRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글 내용을 입력 후 댓글이 생성됩니다.")
    @PostMapping("/api/auth/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(commentRequestDto, request);
    }

    @Operation(summary = "댓글 수정", description = "commentId에 해당하는 댓글을 수정합니다.")
    @PutMapping("/api/auth/comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(commentId, commentRequestDto, request);
    }

    @Operation(summary = "댓글 삭제", description = "commentId에 해당하는 댓글을 삭제합니다.")
    @DeleteMapping("/api/auth/comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }
}
