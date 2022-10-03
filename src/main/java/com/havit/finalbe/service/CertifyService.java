package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.CertifyRequestDto;
import com.havit.finalbe.dto.response.CertifyResponseDto;
import com.havit.finalbe.dto.response.CommentResponseDto;
import com.havit.finalbe.dto.response.SubCommentResponseDto;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.*;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CertifyService {

    private final CertifyRepository certifyRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final ParticipateRepository participateRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final ImageService imageService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CertifyResponseDto createCertify(CertifyRequestDto certifyRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(certifyRequestDto.getGroupId());
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }
        Groups findGroup = groupRepository.findByGroupId(groups.getGroupId());
        Long memberId = findGroup.getMember().getMemberId();

//        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isEmpty()
//        && !memberId.equals(member.getMemberId())) {
//            throw new IllegalArgumentException("참여 내역이 없습니다.");
//        }

        // 참여자면 leaderName 또는 crewName 추출하는 코드 추가 작성칸

        Certify certify = Certify.builder()
                .member(member)
                .groups(groups)
                .title(certifyRequestDto.getTitle())
                .imageId(certifyRequestDto.getImageId())
                .latitude(certifyRequestDto.getLatitude())
                .longitude(certifyRequestDto.getLongitude())
                .build();

        certifyRepository.save(certify);

        return CertifyResponseDto.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imageId(certify.getImageId())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileImageId(certify.getMember().getImageId())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .build();
    }

    @Transactional(readOnly = true)
    public CertifyResponseDto getCertifyDetail(Long certifyId) {

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new CustomException(ErrorCode.CERTIFY_NOT_FOUND);
        }

        List<Comment> commentList = commentRepository.findAllByCertify(certify);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {

            List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
            List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();

            for (SubComment subComment : subCommentList) {

                subCommentResponseDtoList.add(
                        SubCommentResponseDto.builder()
                                .subCommentId(subComment.getSubCommentId())
                                .commentId(subComment.getComment().getCommentId())
                                .nickname(subComment.getMember().getNickname())
                                .profileImageId(subComment.getMember().getImageId())
                                .content(subComment.getContent())
                                .dateTime(serviceUtil.getDateFormatOfSubComment(subComment))
                                .build()
                );
            }
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getCommentId())
                            .certifyId(comment.getCertify().getCertifyId())
                            .nickname(comment.getMember().getNickname())
                            .profileImageId(comment.getMember().getImageId())
                            .content(comment.getContent())
                            .dateTime(serviceUtil.getDateFormatOfComment(comment))
                            .subCommentList(subCommentResponseDtoList)
                            .build()
            );
        }
        return CertifyResponseDto.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imageId(certify.getImageId())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileImageId(certify.getMember().getImageId())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .commentList(commentResponseDtoList)
                        .build();
    }

    @Transactional
    public CertifyResponseDto updateCertify(Long certifyId, CertifyRequestDto certifyRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new CustomException(ErrorCode.CERTIFY_NOT_FOUND);
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        // leaderName 또는 crewName 추출하는 코드 추가 작성칸

        Long originImage = certify.getImageId();
        imageService.deleteImage(originImage);
        certify.update(certifyRequestDto);

        return CertifyResponseDto.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imageId(certify.getImageId())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileImageId(certify.getMember().getImageId())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .build();
    }

    @Transactional
    public String deleteCertify(Long certifyId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new CustomException(ErrorCode.CERTIFY_NOT_FOUND);
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        Long originImage = certify.getImageId();
        imageService.deleteImage(originImage);
        certifyRepository.delete(certify);

        return "삭제가 완료되었습니다.";
    }


    // 해당 인증샷 id 유무 확인
    @Transactional(readOnly = true)
    public Certify isPresentCertify(Long certifyId) {
        Optional<Certify> certifyOptional = certifyRepository.findById(certifyId);
        return certifyOptional.orElse(null);
    }
}
