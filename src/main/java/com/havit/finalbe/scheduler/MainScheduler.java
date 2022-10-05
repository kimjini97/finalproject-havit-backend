package com.havit.finalbe.scheduler;

import com.havit.finalbe.dto.response.UnchallengerResponseDto;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Participate;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.GroupRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainScheduler {
    private final ParticipateRepository participateRepository;
    private final GroupRepository groupRepository;
    private final CertifyRepository certifyRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public List<UnchallengerResponseDto> getUnchallengerList(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        // 현재 날짜 기준으로 어제 날짜 구하기
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        LocalDate yesterday = LocalDate.parse(dateFormat.format(calendar.getTime()));

        // 내가 작성한 그룹, 참여한 그룹 불러오기
        List<Participate> getMyGroupList = participateRepository.findAllByMember_MemberId(member.getMemberId());
        List<Groups> getMyGroups = groupRepository.findAllByMember_MemberId(member.getMemberId());

        if (getMyGroupList.isEmpty() && getMyGroups.isEmpty()) {
            throw new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND);
        }

        List<Groups> getAllGroupList = new ArrayList<>();

        // 내가 작성한 그룹, 참여한 그룹에서 그룹 객체만 따로 빼기
        for (Participate myParticipate : getMyGroupList) {
            getAllGroupList.add(myParticipate.getGroups());
        }

        for (Groups myGroups : getMyGroups) {
            getAllGroupList.add(myGroups);
        }

        List<UnchallengerResponseDto> unchallengerResponseDtoList = new ArrayList<>();

        // 내가 작성한 그룹, 참여한 그룹에 해당하는 다른 참여자들 불러오기
        for (Groups groups : getAllGroupList) {
            List<Participate> participateList = participateRepository.findAllByGroups(groups);
            if (participateList.isEmpty()) {
                throw new CustomException(ErrorCode.CHALLENGERS_NOT_FOUND);
            }

            for (Participate participate : participateList) {
                Member getMember = participate.getMember();

                // 어제 날짜 기준으로 참여자와 그룹에 해당하는 인증샷이 존재하지 않으면 참여하지 않은 것으로 판단 -> 참여하지 않은 사람 목록에 추가
                if (certifyRepository.findByCreatedDateAndMember_MemberIdAndGroups_GroupId(yesterday, getMember.getMemberId(), groups.getGroupId()).isEmpty()) {
                    UnchallengerResponseDto unchallengerResponseDto = UnchallengerResponseDto.builder()
                            .groupId(groups.getGroupId())
                            .member(getMember)
                            .build();
                    unchallengerResponseDtoList.add(unchallengerResponseDto);
                }
            }
        }

        return unchallengerResponseDtoList;
    }
}
