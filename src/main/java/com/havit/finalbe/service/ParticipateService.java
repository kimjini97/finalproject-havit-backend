package com.havit.finalbe.service;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Participate;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ParticipateService {

    private final ParticipateRepository participateRepository;
    private final CertifyRepository certifyRepository;
    private final GroupService groupService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public GroupDto.Response participate(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(groupId);
        if (null == groups) {
            throw new IllegalArgumentException("해당 그룹을 찾을 수 없습니다.");
        }

        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isPresent()) {
            throw new IllegalArgumentException("이미 참여 중인 그룹입니다.");
        }

        Participate participate = Participate.builder()
                .groups(groups)
                .member(member)
                .build();
        participateRepository.save(participate);

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
        List<Long> certifyImageIdList = new ArrayList<>();
        for (Certify imageId : certifyList) {
            certifyImageIdList.add(imageId.getImageId());
        }

        return GroupDto.Response.builder()
                .groupId(groupId)
                .title(groups.getTitle())
                .nickname(groups.getMember().getNickname())
                .leaderName(groups.getLeaderName())
                .crewName(groups.getCrewName())
                .startDate(groups.getStartDate())
                .content(groups.getContent())
                .imageId(groups.getImageId())
                .createdAt(groups.getCreatedAt())
                .modifiedAt(groups.getModifiedAt())
                .groupTag(tagListByGroup)
                .memberCount(memberCount)
                .memberList(memberList)
                .certifyImageIdList(certifyImageIdList)
                .build();
    }

    @Transactional
    public GroupDto.Response cancelParticipation(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(groupId);
        if (null == groups) {
            throw new IllegalArgumentException("해당 그룹을 찾을 수 없습니다.");
        }

        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isEmpty()) {
            throw new IllegalArgumentException("참여 내역이 없습니다.");

        }

        Optional<Participate> participation = participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId());
        Long participateId = participation.get().getParticipateId();

        Participate participate = Participate.builder()
                .participateId(participateId)
                .groups(groups)
                .member(member)
                .build();
        participateRepository.delete(participate);

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
        List<Long> certifyImageIdList = new ArrayList<>();
        for (Certify imageId : certifyList) {
            certifyImageIdList.add(imageId.getImageId());
        }

        return GroupDto.Response.builder()
                .groupId(groupId)
                .title(groups.getTitle())
                .nickname(groups.getMember().getNickname())
                .leaderName(groups.getLeaderName())
                .crewName(groups.getCrewName())
                .startDate(groups.getStartDate())
                .content(groups.getContent())
                .imageId(groups.getImageId())
                .createdAt(groups.getCreatedAt())
                .modifiedAt(groups.getModifiedAt())
                .groupTag(tagListByGroup)
                .memberCount(memberCount)
                .memberList(memberList)
                .certifyImageIdList(certifyImageIdList)
                .build();
    }
}
