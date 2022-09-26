package com.havit.finalbe.service;

import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.SubComment;
import com.havit.finalbe.repository.SubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final CommentService commentService;
    private final ServiceUtil serviceUtil;


    @Transactional
    public ResponseDto<?> createSubComment(SubCommentDto.Request subCommentDto, HttpServletRequest request) {

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

        Comment comment = commentService.isPresentComment(subCommentDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        SubComment subComment = SubComment.builder()
                .member(member)
                .comment(comment)
                .content(subCommentDto.getContent())
                .build();

        subCommentRepository.save(subComment);

        return ResponseDto.success(
                SubCommentDto.Response.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileUrl(subComment.getMember().getProfileUrl())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updateSubComment(Long subCommentId, SubCommentDto.Request subCommentDto, HttpServletRequest request) {

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

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        subComment.update(subCommentDto);

        return ResponseDto.success(
                SubCommentDto.Response.builder()
                        .subCommentId(subComment.getSubCommentId())
                        .commentId(subComment.getComment().getCommentId())
                        .nickname(subComment.getMember().getNickname())
                        .profileUrl(subComment.getMember().getProfileUrl())
                        .content(subComment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteSubComment(Long subCommentId, HttpServletRequest request) {

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

        SubComment subComment = isPresentSubComment(subCommentId);
        if (null == subComment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (!subComment.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        subCommentRepository.delete(subComment);

        return ResponseDto.success("삭제가 완료되었습니다.");
    }


    // 해당 대댓글 id 유무 확인
    @Transactional(readOnly = true)
    public SubComment isPresentSubComment(Long subCommentId) {
        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        return subCommentOptional.orElse(null);
    }
}
