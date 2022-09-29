package com.havit.finalbe.service;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.dto.MemberDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String checkPassword(MemberDto.CheckPassword checkPasswordDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        if(!member.validatePassword(passwordEncoder, checkPasswordDto.getPassword())) {
            return "false";
        }

        return "true";
    }

    public List<GroupDto.AllGroupList> getMyGroup(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        List<Groups> myGroups = groupRepository.findAllByMember_MemberId(member.getMemberId());
        List<Participate> myParticipation = participateRepository.findAllByMember_MemberId(member.getMemberId());

        if (myGroups.isEmpty() && myParticipation.isEmpty()) {
            throw new IllegalArgumentException("참여 내역이 없습니다.");
        }

        List<GroupDto.AllGroupList> allMyGroupList = new ArrayList<>();

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

            // 로그인한 멤버의 계급 확인 코드 ( 만약 추가하면 AllGroupListResponseDto 에도 필드 추가해야 함 )

            GroupDto.AllGroupList MyGroupDto = GroupDto.AllGroupList.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imgUrl(groups.getImgUrl())
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
    public MemberDto.Response editMyInfo(MemberDto.MyPage myPageDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        String imgUrl = "";
        MultipartFile imgFile = myPageDto.getImgFile();
        if (null == imgFile || imgFile.isEmpty()) {
            if (null == findMember.getProfileUrl()) {
                imgUrl = null;
            } else if (!findMember.getProfileUrl().isEmpty()) {
                imgUrl = findMember.getProfileUrl();
            }
        } else {
            imgUrl = serviceUtil.uploadImage(imgFile, "profile");
        }

        String nickname = myPageDto.getNickname();
        if (nickname.isEmpty()) {
            nickname = member.getNickname();
        }

        String introduce = myPageDto.getIntroduce();
        if (null == introduce || introduce.isEmpty()) {
            if (null == findMember.getIntroduce()) {
                introduce = null;
            } else if (!findMember.getIntroduce().isEmpty()) {
                introduce = findMember.getIntroduce();
            }
        }

        String password = myPageDto.getPassword();
        String passwordConfirm = myPageDto.getPasswordConfirm();
        if (password.isEmpty()) {
            password = member.getPassword();
            findMember.edit(imgUrl, nickname, introduce, password);

            return MemberDto.Response.builder()
                            .memberId(findMember.getMemberId())
                            .username(findMember.getUsername())
                            .nickname(findMember.getNickname())
                            .profileUrl(findMember.getProfileUrl())
                            .introduce(findMember.getIntroduce())
                            .createdAt(findMember.getCreatedAt())
                            .modifiedAt(findMember.getModifiedAt())
                            .build();
        }

        if (!passwordStrCheck(password)) {
            throw new InvalidPasswordException(ErrorMsg.INVALID_PASSWORD);
        } else if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } else {
            findMember.edit(imgUrl, nickname, introduce, passwordEncoder.encode(password));
        }

        return MemberDto.Response.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .profileUrl(findMember.getProfileUrl())
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build();
    }

    @Transactional
    public MemberDto.Response deleteProfile(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        String originFile = findMember.getProfileUrl();
        if (null == originFile) {
            throw new IllegalArgumentException("삭제할 이미지가 없습니다.");
        }
        String key = originFile.substring(52);
        serviceUtil.deleteImage(key);

        findMember.deleteImg(null);

        return MemberDto.Response.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .profileUrl(findMember.getProfileUrl())
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build();
    }

    private boolean passwordStrCheck (String password) {
        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
    }
}
