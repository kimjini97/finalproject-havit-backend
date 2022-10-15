package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.CheckPasswordRequestDto;
import com.havit.finalbe.dto.request.MyPageRequestDto;
import com.havit.finalbe.dto.request.MyPasswordRequestDto;
import com.havit.finalbe.dto.response.AllGroupListResponseDto;
import com.havit.finalbe.dto.response.MemberResponseDto;
import com.havit.finalbe.entity.Favorite;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Participate;
import com.havit.finalbe.exception.*;
import com.havit.finalbe.repository.FavoriteRepository;
import com.havit.finalbe.repository.GroupRepository;
import com.havit.finalbe.repository.MemberRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class MyPageService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ParticipateRepository participateRepository;
    private final FavoriteRepository favoriteRepository;
    private final ServiceUtil serviceUtil;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String checkPassword(CheckPasswordRequestDto checkPasswordDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        if(!member.validatePassword(passwordEncoder, checkPasswordDto.getPassword())) {
            return "false";
        }

        return "true";
    }

    public List<AllGroupListResponseDto> getMyGroup(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
//        List<Groups> myGroups = groupRepository.findAllByMember_MemberId(member.getMemberId());
        List<Groups> myGroups = new ArrayList<>();
        List<Participate> myParticipation = participateRepository.findAllByMember_MemberId(member.getMemberId());

//        if (myGroups.isEmpty() && myParticipation.isEmpty()) {
//            throw new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND);
//        }

        if (myParticipation.isEmpty()) {
            throw new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND);
        }

        List<AllGroupListResponseDto> allMyGroupList = new ArrayList<>();

        for (Participate participate : myParticipation) {
            Groups myJoinGroups = groupRepository.findByGroupId(participate.getGroups().getGroupId());
            myGroups.add(myJoinGroups);
        }

        for (Groups groups : myGroups) {
            boolean isFavorites = false;
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository
                    .findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            AllGroupListResponseDto MyGroupDto = AllGroupListResponseDto.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imageId(groups.getImageId())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allMyGroupList.add(MyGroupDto);
        }

        return allMyGroupList;
    }

    @Transactional
    public MemberResponseDto editMyInfo(MyPageRequestDto myPageDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        Long imageId = myPageDto.getImageId();
        if (null == imageId) {
            imageId = findMember.getImageId();
        }

        if (null != findMember.getImageId()) {
            if (!findMember.getImageId().equals(imageId)) {
                imageService.deleteImage(findMember.getImageId());
            }
        }

        String nickname = myPageDto.getNickname();
        if (nickname.isEmpty()) {
            nickname = findMember.getNickname();
        }

        String introduce = myPageDto.getIntroduce();
        if (null == introduce || introduce.isEmpty()) {
            if (null == findMember.getIntroduce()) {
                introduce = null;
            } else if (!findMember.getIntroduce().isEmpty()) {
                introduce = findMember.getIntroduce();
            }
        }

        findMember.update(imageId, nickname, introduce);

        return MemberResponseDto.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .imageId(findMember.getImageId())
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build();
    }

    @Transactional
    public MemberResponseDto editMyPassword(MyPasswordRequestDto myPassDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        String password = myPassDto.getPassword();
        String passwordConfirm = myPassDto.getPasswordConfirm();
        if (password.isEmpty()) {
            password = member.getPassword();
            findMember.edit(password);

            return MemberResponseDto.builder()
                    .memberId(findMember.getMemberId())
                    .username(findMember.getUsername())
                    .nickname(findMember.getNickname())
                    .imageId(findMember.getImageId())
                    .introduce(findMember.getIntroduce())
                    .createdAt(findMember.getCreatedAt())
                    .modifiedAt(findMember.getModifiedAt())
                    .build();
        }

        if (!passwordStrCheck(password)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        } else if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        } else {
            findMember.edit(passwordEncoder.encode(password));
        }

        return MemberResponseDto.builder()
                .memberId(findMember.getMemberId())
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .imageId(findMember.getImageId())
                .introduce(findMember.getIntroduce())
                .createdAt(findMember.getCreatedAt())
                .modifiedAt(findMember.getModifiedAt())
                .build();
    }

    @Transactional
    public MemberResponseDto deleteProfile(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        Long originImage = findMember.getImageId();
        if (null == originImage) {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }

        imageService.deleteImage(originImage);
        findMember.deleteImg(null);

        return MemberResponseDto.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .imageId(findMember.getImageId())
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build();
    }

    private boolean passwordStrCheck (String password) {
        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
    }
}
