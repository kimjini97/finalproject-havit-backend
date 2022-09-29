package com.havit.finalbe.service;

import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.SubComment;
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
    public SubCommentDto.Response createSubComment(SubCommentDto.Request subCommentDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = commentService.isPresentComment(subCommentDto.getCommentId());
        if (null == comment) {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
        }

        SubComment subComment = SubComment.builder()
                .member(member)
                .comment(comment)
                .content(subCommentDto.getContent())
                .build();

        subCommentRepository.save(subComment);

        return SubCommentDto.Response.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileUrl(subComment.getMember().getProfileUrl())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build();
    }

    @Transactional
    public SubCommentDto.Response updateSubComment(Long subCommentId, SubCommentDto.Request subCommentDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        subComment.update(subCommentDto);

        return SubCommentDto.Response.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileUrl(subComment.getMember().getProfileUrl())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build();
    }

    @Transactional
    public String deleteSubComment(Long subCommentId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
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
