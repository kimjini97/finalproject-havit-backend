package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.GroupRequestDto;
import com.havit.finalbe.dto.response.AllGroupListResponseDto;
import com.havit.finalbe.dto.response.CertifyResponseDto;
import com.havit.finalbe.dto.response.GroupResponseDto;
import com.havit.finalbe.entity.Groups;
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
public class GroupService {

    private final GroupRepository groupRepository;
    private final CertifyRepository certifyRepository;
    private final TagsRepository tagsRepository;
    private final GroupTagRepository groupTagRepository;
    private final ParticipateRepository participateRepository;
    private final FavoriteRepository favoriteRepository;
    private final ImageService imageService;
    private final ServiceUtil serviceUtil;

    // 그룹 생성
    @Transactional
    public GroupResponseDto createGroup(GroupRequestDto groupRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = Groups.builder()
                .member(member)
                .title(groupRequestDto.getTitle())
                .imageId(groupRequestDto.getImageId())
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

        Participate participate = Participate.builder()
                .groups(saveGroups)
                .member(member)
                .build();
        participateRepository.save(participate);

        return GroupResponseDto.builder()
                        .groupId(groups.getGroupId())
                        .title(groups.getTitle())
                        .imageId(groups.getImageId())
                        .writer(groups.getMember())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .content(groups.getContent())
                        .groupTag(tagListByGroup)
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .build();
    }

    // 그룹 전체 목록 조회
    @Transactional(readOnly = true)
    public List<AllGroupListResponseDto> getAllGroup(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        List<Groups> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
        List<AllGroupListResponseDto> allGroupListResponseDtoList = new ArrayList<>();

        for (Groups groups : groupList) {
            boolean isFavorites = false;
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository
                    .findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            AllGroupListResponseDto allGroupListResponseDto = AllGroupListResponseDto.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imageId(groups.getImageId())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allGroupListResponseDtoList.add(allGroupListResponseDto);
        }
        return allGroupListResponseDtoList;
    }

    // 태그별 그룹 전체 목록 조회
    @Transactional(readOnly = true)
    public List<AllGroupListResponseDto> getAllGroupByTag(UserDetailsImpl userDetails, String keyword) {

        Member member = userDetails.getMember();

        Tags tags = tagsRepository.findByTagName(keyword);
        if (null == tags) {
            throw new CustomException(ErrorCode.TAG_NOT_FOUND);
        }

        List<GroupTag> groupTagList = groupTagRepository.findAllByTagsOrderByGroupsDesc(tags);
        List<AllGroupListResponseDto> allGroupListResponseDtoList = new ArrayList<>();

        for (GroupTag groupTag : groupTagList) {
            boolean isFavorites = false;
            Groups groups = groupTag.getGroups();
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository.findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            AllGroupListResponseDto allGroupListResponseDto = AllGroupListResponseDto.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imageId(groups.getImageId())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allGroupListResponseDtoList.add(allGroupListResponseDto);
        }
        return allGroupListResponseDtoList;
    }

    // 그룹 상세 조회
    @Transactional(readOnly = true)
    public GroupResponseDto getGroupDetail(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
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

        // 인증샷 목록 가져오기
        List<Certify> certifyList = certifyRepository.findByGroups_GroupIdOrderByCreatedAtDesc(groupId);
        List<CertifyResponseDto> certifyResponseDtoList = new ArrayList<>();

        for (Certify certify : certifyList) {
            certifyResponseDtoList.add(
                    CertifyResponseDto.builder()
                            .certifyId(certify.getCertifyId())
                            .groupId(certify.getGroups().getGroupId())
                            .title(certify.getTitle())
                            .imageId(certify.getImageId())
                            .longitude(certify.getLongitude())
                            .latitude(certify.getLatitude())
                            .nickname(certify.getMember().getNickname())
                            .profileImageId(certify.getMember().getImageId())
                            .createdAt(certify.getCreatedAt())
                            .modifiedAt(certify.getModifiedAt())
                            .build()
            );
        }

        return GroupResponseDto.builder()
                        .groupId(groupId)
                        .title(groups.getTitle())
                        .writer(groups.getMember())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .content(groups.getContent())
                        .imageId(groups.getImageId())
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .groupTag(tagListByGroup)
                        .memberCount(memberCount)
                        .memberList(memberList)
                        .certifyList(certifyResponseDtoList)
                        .build();
    }

    // 그룹 수정
    @Transactional
    public GroupResponseDto updateGroup(Long groupId, GroupRequestDto groupRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        if (!groups.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        Long originImage = groups.getImageId();
        imageService.deleteImage(originImage);
        groups.update(groupRequestDto);

        if (null == groupRequestDto.getGroupTag()) {
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            return GroupResponseDto.builder()
                            .groupId(groups.getGroupId())
                            .title(groups.getTitle())
                            .imageId(groups.getImageId())
                            .writer(groups.getMember())
                            .leaderName(groups.getLeaderName())
                            .crewName(groups.getCrewName())
                            .content(groups.getContent())
                            .groupTag(tagListByGroup)
                            .createdAt(groups.getCreatedAt())
                            .modifiedAt(groups.getModifiedAt())
                            .build();
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

        return GroupResponseDto.builder()
                        .groupId(groups.getGroupId())
                        .title(groups.getTitle())
                        .imageId(groups.getImageId())
                        .writer(groups.getMember())
                        .leaderName(groups.getLeaderName())
                        .crewName(groups.getCrewName())
                        .content(groups.getContent())
                        .groupTag(tagListByGroup)
                        .createdAt(groups.getCreatedAt())
                        .modifiedAt(groups.getModifiedAt())
                        .build();
    }

    // 그룹 삭제
    @Transactional
    public String deleteGroup(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        if (!groups.getMember().isValidateMember(member.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCHED);
        }

        Long originFile = groups.getImageId();
        imageService.deleteImage(originFile);

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
        return "삭제가 완료되었습니다.";
    }

    // 그룹 존재 여부 체크
    @Transactional(readOnly = true)
    public Groups isPresentGroup(Long groupId) {
        Optional<Groups> groupOptional = groupRepository.findById(groupId);
        return groupOptional.orElse(null);
    }
}
