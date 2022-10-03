package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.CommentRequestDto;
import com.havit.finalbe.dto.response.CommentResponseDto;
import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.CommentRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CertifyService certifyService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = certifyService.isPresentCertify(commentRequestDto.getCertifyId());
        if (null == certify) {
            throw new CustomException(ErrorCode.CERTIFY_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .member(member)
                .certify(certify)
                .content(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .certifyId(comment.getCertify().getCertifyId())
                        .nickname(comment.getMember().getNickname())
                        .profileImageId(comment.getMember().getImageId())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        comment.update(commentRequestDto);

        return CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .certifyId(comment.getCertify().getCertifyId())
                        .nickname(comment.getMember().getNickname())
                        .profileImageId(comment.getMember().getImageId())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build();
    }

    @Transactional
    public String deleteComment(Long commentId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        commentRepository.delete(comment);

        return "삭제가 완료되었습니다.";
    }


    // 해당 댓글 id 유무 확인
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElse(null);
    }
}
