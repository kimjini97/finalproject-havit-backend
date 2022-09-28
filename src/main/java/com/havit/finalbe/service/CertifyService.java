package com.havit.finalbe.service;

import com.havit.finalbe.dto.CommentDto;
import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.dto.CertifyDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.CommentRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.repository.SubCommentRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class CertifyService {

    private final CertifyRepository certifyRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final ParticipateRepository participateRepository;
    private final GroupService groupService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<?> createCertify(CertifyDto.Request certifyRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(certifyRequestDto.getGroupId());
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isEmpty()) {
            return ResponseDto.fail(PARTICIPATION_NOT_FOUND);
        }

        // 참여자면 leaderName 또는 crewName 추출하는 코드 추가 작성칸

        String imgUrl = "";
        MultipartFile imgFile = certifyRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            imgUrl = serviceUtil.uploadImage(imgFile, "certify");
        }

        Certify certify = Certify.builder()
                .member(member)
                .groups(groups)
                .title(certifyRequestDto.getTitle())
                .imgUrl(imgUrl)
                .latitude(certifyRequestDto.getLatitude())
                .longitude(certifyRequestDto.getLongitude())
                .build();

        certifyRepository.save(certify);

        return ResponseDto.success(
                CertifyDto.Response.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imgUrl(certify.getImgUrl())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileUrl(certify.getMember().getProfileUrl())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getCertifyDetail(Long certifyId) {

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        List<Comment> commentList = commentRepository.findAllByCertify(certify);
        List<CommentDto.Response> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {

            List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
            List<SubCommentDto.Response> subCommentResponseDtoList = new ArrayList<>();

            for (SubComment subComment : subCommentList) {

                subCommentResponseDtoList.add(
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
            commentResponseDtoList.add(
                    CommentDto.Response.builder()
                            .commentId(comment.getCommentId())
                            .certifyId(comment.getCertify().getCertifyId())
                            .nickname(comment.getMember().getNickname())
                            .profileUrl(comment.getMember().getProfileUrl())
                            .content(comment.getContent())
                            .dateTime(serviceUtil.getDateFormatOfComment(comment))
                            .subCommentList(subCommentResponseDtoList)
                            .build()
            );
        }
        return ResponseDto.success(
                CertifyDto.Response.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imgUrl(certify.getImgUrl())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileUrl(certify.getMember().getProfileUrl())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .commentList(commentResponseDtoList)
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updateCertify(Long certifyId, CertifyDto.Request certifyRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        // leaderName 또는 crewName 추출하는 코드 추가 작성칸

        String originFile = certify.getImgUrl();
        String key = originFile.substring(52);
        String imgUrl = "";
        MultipartFile imgFile = certifyRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            serviceUtil.deleteImage(key);
            imgUrl = serviceUtil.uploadImage(imgFile, "certify");
            certify.update(certifyRequestDto, imgUrl);
        }

        certify.update(certifyRequestDto, originFile);

        return ResponseDto.success(
                CertifyDto.Response.builder()
                        .certifyId(certify.getCertifyId())
                        .groupId(certify.getGroups().getGroupId())
                        .title(certify.getTitle())
                        .imgUrl(certify.getImgUrl())
                        .longitude(certify.getLongitude())
                        .latitude(certify.getLatitude())
                        .nickname(certify.getMember().getNickname())
                        // leaderName
                        // crewName
                        .profileUrl(certify.getMember().getProfileUrl())
                        .createdAt(certify.getCreatedAt())
                        .modifiedAt(certify.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteCertify(Long certifyId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        String originFile = certify.getImgUrl();
        String key = originFile.substring(52);

        serviceUtil.deleteImage(key);
        certifyRepository.delete(certify);

        return ResponseDto.success("삭제가 완료되었습니다.");
    }


    // 해당 인증샷 id 유무 확인
    @Transactional(readOnly = true)
    public Certify isPresentCertify(Long certifyId) {
        Optional<Certify> certifyOptional = certifyRepository.findById(certifyId);
        return certifyOptional.orElse(null);
    }
}
