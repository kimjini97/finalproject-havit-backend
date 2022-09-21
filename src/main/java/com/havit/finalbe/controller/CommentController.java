package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.CommentRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/auth/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(commentRequestDto, request);
    }

    @PutMapping("/api/auth/comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(commentId, commentRequestDto, request);
    }

    @DeleteMapping("/api/auth/comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }
}
