package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.CertifyRequestDto;
import com.havit.finalbe.dto.response.CertifyResponseDto;
import com.havit.finalbe.dto.response.CommentResponseDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.dto.response.SubCommentResponseDto;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.CommentRepository;
import com.havit.finalbe.repository.SubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    private final GroupService groupService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<?> createCertify(CertifyRequestDto certifyRequestDto, HttpServletRequest request) throws IOException {

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

        Groups groups = groupService.isPresentGroup(certifyRequestDto.getGroupId());
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        // 멤버가 참여자인지 확인하고, (참여자 아닐 경우 fail) 참여자면 leaderName 또는 crewName 추출하는 코드 추가 작성칸

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
                CertifyResponseDto.builder()
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
                                .profileUrl(subComment.getMember().getProfileUrl())
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
                            .profileUrl(comment.getMember().getProfileUrl())
                            .content(comment.getContent())
                            .dateTime(serviceUtil.getDateFormatOfComment(comment))
                            .subCommentList(subCommentResponseDtoList)
                            .build()
            );
        }
        return ResponseDto.success(
                CertifyResponseDto.builder()
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
    public ResponseDto<?> updateCertify(Long certifyId, CertifyRequestDto certifyRequestDto, HttpServletRequest request) throws IOException {

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

        Certify certify = isPresentCertify(certifyId);
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        if (!certify.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        // leaderName 또는 crewName 추출하는 코드 추가 작성칸

        String originFile = certify.getImgUrl();
        String key = originFile.substring(60);
        String imgUrl = "";
        MultipartFile imgFile = certifyRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            serviceUtil.deleteImage(key);
            imgUrl = serviceUtil.uploadImage(imgFile, "certify");
        }

        certify.update(certifyRequestDto, imgUrl);

        return ResponseDto.success(
                CertifyResponseDto.builder()
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
    public ResponseDto<?> deleteCertify(Long certifyId, HttpServletRequest request) {

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
