package com.havit.finalbe.service;

import com.havit.finalbe.dto.CommentDto;
import com.havit.finalbe.dto.SubCommentDto;
import com.havit.finalbe.dto.CertifyDto;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.repository.*;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final ServiceUtil serviceUtil;

    @Transactional
    public CertifyDto.Response createCertify(CertifyDto.Request certifyRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(certifyRequestDto.getGroupId());
        if (null == groups) {
            throw new IllegalArgumentException("해당 그룹을 찾을 수 없습니다.");
        }
        Groups findGroup = groupRepository.findByGroupId(groups.getGroupId());
        Long memberId = findGroup.getMember().getMemberId();

//        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isEmpty()
//        && !memberId.equals(member.getMemberId())) {
//            throw new IllegalArgumentException("참여 내역이 없습니다.");
//        }

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

        return CertifyDto.Response.builder()
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
                        .build();
    }

    @Transactional(readOnly = true)
    public CertifyDto.Response getCertifyDetail(Long certifyId) {

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new IllegalArgumentException("해당 인증샷을 찾을 수 없습니다.");
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
        return CertifyDto.Response.builder()
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
                        .build();
    }

    @Transactional
    public CertifyDto.Response updateCertify(Long certifyId, CertifyDto.Request certifyRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new IllegalArgumentException("해당 인증샷을 찾을 수 없습니다.");
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
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
        } else {
            certify.update(certifyRequestDto, originFile);
        }

        return CertifyDto.Response.builder()
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
                        .build();
    }

    @Transactional
    public String deleteCertify(Long certifyId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            throw new IllegalArgumentException("해당 인증샷을 찾을 수 없습니다.");
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        String originFile = certify.getImgUrl();
        String key = originFile.substring(52);

        serviceUtil.deleteImage(key);
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
