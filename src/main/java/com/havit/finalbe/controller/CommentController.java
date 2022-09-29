package com.havit.finalbe.controller;

import com.havit.finalbe.dto.CommentDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"댓글 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글 내용을 입력 후 댓글이 생성됩니다.")
    @PostMapping("/")
    public ResponseDto<CommentDto.Response> createComment(@RequestBody CommentDto.Request commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, userDetails);
    }

    @Operation(summary = "댓글 수정", description = "commentId에 해당하는 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseDto<CommentDto.Response> updateComment(@PathVariable Long commentId, @RequestBody CommentDto.Request commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails);
    }

    @Operation(summary = "댓글 삭제", description = "commentId에 해당하는 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseDto<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }
}
