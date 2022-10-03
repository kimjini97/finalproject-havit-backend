package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.SubCommentRequestDto;
import com.havit.finalbe.dto.response.SubCommentResponseDto;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.SubComment;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.SubCommentRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final CommentService commentService;
    private final ServiceUtil serviceUtil;


    @Transactional
    public SubCommentResponseDto createSubComment(SubCommentRequestDto subCommentDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = commentService.isPresentComment(subCommentDto.getCommentId());
        if (null == comment) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        SubComment subComment = SubComment.builder()
                .member(member)
                .comment(comment)
                .content(subCommentDto.getContent())
                .build();

        subCommentRepository.save(subComment);

        return SubCommentResponseDto.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileImageId(subComment.getMember().getImageId())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build();
    }

    @Transactional
    public SubCommentResponseDto updateSubComment(Long subCommentId, SubCommentRequestDto subCommentDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        subComment.update(subCommentDto);

        return SubCommentResponseDto.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileImageId(subComment.getMember().getImageId())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build();
    }

    @Transactional
    public String deleteSubComment(Long subCommentId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        subCommentRepository.delete(subComment);

        return "삭제가 완료되었습니다.";
    }


    // 해당 대댓글 id 유무 확인
    @Transactional(readOnly = true)
    public SubComment isPresentSubComment(Long subCommentId) {
        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        return subCommentOptional.orElse(null);
    }
}
