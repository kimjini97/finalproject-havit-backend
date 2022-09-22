package com.havit.finalbe.service;


import com.havit.finalbe.dto.request.CommentRequestDto;
import com.havit.finalbe.dto.response.CommentResponseDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CertifyService certifyService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Certify certify = certifyService.isPresentCertify(commentRequestDto.getCertifyId());
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .member(member)
                .certify(certify)
                .content(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .nickname(comment.getMember().getNickname())
                        .profileUrl(comment.getMember().getProfileUrl())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (comment.isValidateMember(member)) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        comment.update(commentRequestDto);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .nickname(comment.getMember().getNickname())
                        .profileUrl(comment.getMember().getProfileUrl())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (comment.isValidateMember(member)) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        commentRepository.delete(comment);

        return ResponseDto.success("삭제가 완료되었습니다.");
    }


    // 해당 댓글 id 유무 확인
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElse(null);
    }
}
