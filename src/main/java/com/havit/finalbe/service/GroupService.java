package com.havit.finalbe.service;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.dto.response.ResponseDto;
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
import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final CertifyRepository certifyRepository;
    private final TagsRepository tagsRepository;
    private final GroupTagRepository groupTagRepository;
    private final ParticipateRepository participateRepository;
    private final FavoriteRepository favoriteRepository;
    private final ServiceUtil serviceUtil;

    // 그룹 생성
    @Transactional
    public ResponseDto<?> createGroup(GroupDto.Request groupRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        String imgUrl = "";
        MultipartFile imgFile = groupRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            imgUrl = serviceUtil.uploadImage(imgFile, "group");
        }

        Groups groups = Groups.builder()
                .member(member)
                .title(groupRequestDto.getTitle())
                .startDate(groupRequestDto.getStartDate())
                .imgUrl(imgUrl)
                .content(groupRequestDto.getContent())
                .leaderName(groupRequestDto.getLeaderName())
                .crewName(groupRequestDto.getCrewName())
                .build();
        Groups saveGroups = groupRepository.save(groups);

        List<String> tagList = groupRequestDto.getGroupTag();
        if (null != tagList) {
            for (String tagName : tagList) {
                Tags tagInDB = tagsRepository.findByTagName(tagName);
                if (null == tagInDB) {
                    Tags tags = Tags.builder().tagName(tagName).build();
                    Tags saveTags = tagsRepository.save(tags);
                    GroupTag groupTag = GroupTag.builder()
                            .groups(saveGroups)
                            .tags(saveTags)
                            .build();
                    groupTagRepository.save(groupTag);
                } else {
                    GroupTag groupTag = GroupTag.builder()
                            .groups(saveGroups)
                            .tags(tagInDB)
                            .build();
                    groupTagRepository.save(groupTag);
                }
            }
        }

        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        return ResponseDto.success(
                GroupDto.Response.builder()
                        .groupId(groups.getGroupId())
                        .title(groups.getTitle())
                        .imgUrl(groups.getImgUrl())
                        .nickname(groups.getMember().getNickname())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .startDate(groups.getStartDate())
                        .content(groups.getContent())
                        .groupTag(tagListByGroup)
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .build()
        );
    }

    // 그룹 전체 목록 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllGroup(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        List<Groups> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
        List<GroupDto.AllGroupList> allGroupListResponseDtoList = new ArrayList<>();
        boolean isFavorites = false;

        for (Groups groups : groupList) {
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository
                    .findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            // 로그인한 멤버의 계급 확인 코드 ( 만약 추가하면 AllGroupListResponseDto 에도 필드 추가해야 함 )

            GroupDto.AllGroupList allGroupListResponseDto = GroupDto.AllGroupList.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imgUrl(groups.getImgUrl())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allGroupListResponseDtoList.add(allGroupListResponseDto);
        }
        return ResponseDto.success(allGroupListResponseDtoList);
    }

    // 태그별 그룹 전체 목록 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllGroupByTag(UserDetailsImpl userDetails, String keyword) {

        Member member = userDetails.getMember();

        Tags tags = tagsRepository.findByTagName(keyword);
        if (null == tags) {
            return ResponseDto.fail(TAG_NOT_FOUND);
        }

        List<GroupTag> groupTagList = groupTagRepository.findAllByTagsOrderByGroupsDesc(tags);
        List<GroupDto.AllGroupList> allGroupListResponseDtoList = new ArrayList<>();
        boolean isFavorites = false;

        for (GroupTag groupTag : groupTagList) {
            Groups groups = groupTag.getGroups();
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository.findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            // 로그인한 멤버의 계급 확인 코드 ( 만약 추가하면 AllGroupListResponseDto 에도 필드 추가해야 함 )

            GroupDto.AllGroupList allGroupListResponseDto = GroupDto.AllGroupList.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imgUrl(groups.getImgUrl())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allGroupListResponseDtoList.add(allGroupListResponseDto);
        }
        return ResponseDto.success(allGroupListResponseDtoList);
    }

    // 그룹 상세 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getGroupDetail(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        // 만약 참여자면 어떤 계급인지 추출

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        // 태그 가져오기
        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        // 참여한 멤버 카운팅
        int memberCount = participateRepository.countByGroups_GroupId(groupId);

        // 참여 멤버 목록
        List<Participate> participateList = participateRepository.findAllByGroups(groups);
        List<Member> memberList = new ArrayList<>();
        for (Participate participateMember : participateList) {
            memberList.add(participateMember.getMember());
        }

        // 인증샷 이미지 URL 목록 가져오기
        List<Certify> certifyList = certifyRepository.findByGroups_GroupId(groupId);
        List<String> certifyImgUrlList = new ArrayList<>();
        for (Certify imgUrl : certifyList) {
            certifyImgUrlList.add(imgUrl.getImgUrl());
        }

        return ResponseDto.success(
                GroupDto.Response.builder()
                        .groupId(groupId)
                        .title(groups.getTitle())
                        .nickname(groups.getMember().getNickname())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .startDate(groups.getStartDate())
                        .content(groups.getContent())
                        .imgUrl(groups.getImgUrl())
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .groupTag(tagListByGroup)
                        .memberCount(memberCount)
                        .memberList(memberList)
                        .certifyImgUrlList(certifyImgUrlList)
                        .build()
        );
    }

    // 그룹 수정
    @Transactional
    public ResponseDto<?> updateGroup(Long groupId, GroupDto.Request groupRequestDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        if (!groups.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        String originFile = groups.getImgUrl();
        String key = originFile.substring(52);
        String imgUrl = "";
        MultipartFile imgFile = groupRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            serviceUtil.deleteImage(key);
            imgUrl = serviceUtil.uploadImage(imgFile, "group");
            groups.update(groupRequestDto, imgUrl);
        }

        groups.update(groupRequestDto, originFile);

        if (null == groupRequestDto.getGroupTag()) {
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            return ResponseDto.success(
                    GroupDto.Response.builder()
                            .groupId(groups.getGroupId())
                            .title(groups.getTitle())
                            .imgUrl(groups.getImgUrl())
                            .nickname(groups.getMember().getNickname())
                            .leaderName(groups.getLeaderName())
                            .crewName(groups.getCrewName())
                            .startDate(groups.getStartDate())
                            .content(groups.getContent())
                            .groupTag(tagListByGroup)
                            .createdAt(groups.getCreatedAt())
                            .modifiedAt(groups.getModifiedAt())
                            .build()
            );
        }

        groupTagRepository.deleteByGroups(groups);

        List<String> tagList = groupRequestDto.getGroupTag();
        if (null != tagList) {
            for (String tagName : tagList) {
                Tags tagInDB = tagsRepository.findByTagName(tagName);
                if (null == tagInDB) {
                    Tags tags = Tags.builder().tagName(tagName).build();
                    Tags saveTags = tagsRepository.save(tags);
                    GroupTag groupTag = GroupTag.builder()
                            .groups(groups)
                            .tags(saveTags)
                            .build();
                    groupTagRepository.save(groupTag);
                } else {
                    GroupTag groupTag = GroupTag.builder()
                            .groups(groups)
                            .tags(tagInDB)
                            .build();
                    groupTagRepository.save(groupTag);
                }
            }
        }

        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        return ResponseDto.success(
                GroupDto.Response.builder()
                        .groupId(groups.getGroupId())
                        .title(groups.getTitle())
                        .imgUrl(groups.getImgUrl())
                        .nickname(groups.getMember().getNickname())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .startDate(groups.getStartDate())
                        .content(groups.getContent())
                        .groupTag(tagListByGroup)
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .build()
        );
    }

    // 그룹 삭제
    @Transactional
    public ResponseDto<?> deleteGroup(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        if (!groups.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        String originFile = groups.getImgUrl();
        String key = originFile.substring(52);
        serviceUtil.deleteImage(key);

        List<GroupTag> groupTagList = groupTagRepository.findAllByGroups(groups);
        List<Tags> tagsList = new ArrayList<>();

        for (GroupTag groupTag : groupTagList) {
            Tags tags = groupTag.getTags();
            tagsList.add(tags);
        }

        groupRepository.delete(groups);

        for (Tags tags : tagsList) {
            List<GroupTag> groupTagListByTag = groupTagRepository.findAllByTags(tags);
            if (groupTagListByTag.size() < 1) {
                tagsRepository.delete(tags);
            }
        }
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    // 그룹 존재 여부 체크
    @Transactional(readOnly = true)
    public Groups isPresentGroup(Long groupId) {
        Optional<Groups> groupOptional = groupRepository.findById(groupId);
        return groupOptional.orElse(null);
    }
}
