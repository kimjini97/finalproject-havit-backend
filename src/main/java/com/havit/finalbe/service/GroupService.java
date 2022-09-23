package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.GroupRequestDto;
import com.havit.finalbe.dto.response.GroupResponseDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.GroupTag;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Tags;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.GroupRepository;
import com.havit.finalbe.repository.GroupTagRepository;
import com.havit.finalbe.repository.TagsRepository;
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
public class GroupService {

    private final GroupRepository groupRepository;
    private final CertifyRepository certifyRepository;
    private final TagsRepository tagsRepository;
    private final GroupTagRepository groupTagRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<?> createGroup(GroupRequestDto groupRequestDto, HttpServletRequest request) throws IOException {

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
                GroupResponseDto.builder()
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

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllGroup() {
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getGroupDetail(Long groupId) {
        return null;
    }

    @Transactional
    public ResponseDto<?> updateGroup(Long groupId, GroupRequestDto groupRequestDto, HttpServletRequest request) throws IOException {

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

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        if (groups.isValidateMember(member)) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        String originFile = groups.getImgUrl();
        String key = originFile.substring(58);
        String imgUrl = "";
        MultipartFile imgFile = groupRequestDto.getImgFile();
        if (!imgFile.isEmpty()) {
            serviceUtil.deleteImage(key);
            imgUrl = serviceUtil.uploadImage(imgFile, "group");
        }

        groups.update(groupRequestDto, imgUrl);
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
                GroupResponseDto.builder()
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

    @Transactional
    public ResponseDto<?> deleteGroup(Long groupId, HttpServletRequest request) {

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

        Groups groups = isPresentGroup(groupId);
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        if (groups.isValidateMember(member)) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        String originFile = groups.getImgUrl();
        String key = originFile.substring(58);
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


    @Transactional(readOnly = true)
    public Groups isPresentGroup(Long groupId) {
        Optional<Groups> groupOptional = groupRepository.findById(groupId);
        return groupOptional.orElse(null);
    }
}
