package com.havit.finalbe.service;

import com.havit.finalbe.dto.FavoriteDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Favorite;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.FavoriteRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service

public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ParticipateRepository participateRepository;
    private final ServiceUtil serviceUtil;
    private final GroupService groupService;

    @Transactional
    public ResponseDto<FavoriteDto.Response> favorites(FavoriteDto.Request favoriteRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(favoriteRequestDto.getGroupId());
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        // 태그 가져오기
        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        // 참여한 멤버 카운팅
        int memberCount = participateRepository.countByGroups_GroupId(favoriteRequestDto.getGroupId());

        boolean isFavorites = false;
        Favorite checkFavorite = favoriteRepository.findByMemberAndGroups(member, groups);

        if (null == checkFavorite) {
            Favorite favorite = Favorite.builder()
                    .member(member)
                    .groups(groups)
                    .build();
            favoriteRepository.save(favorite);

            Favorite checkMember = favoriteRepository.findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkMember) {
                isFavorites = true;
            }

            return ResponseDto.success(
                    FavoriteDto.Response.builder()
                            .groupId(favorite.getGroups().getGroupId())
                            .title(favorite.getGroups().getTitle())
                            .imgUrl(favorite.getGroups().getImgUrl())
                            .memberCount(memberCount)
                            .groupTag(tagListByGroup)
                            .favorite(isFavorites)
                            .createdAt(favorite.getGroups().getCreatedAt())
                            .modifiedAt(favorite.getGroups().getModifiedAt())
                            .member(checkMember.getMember())
                            .build()
            );
        }

        Favorite favorite = Favorite.builder()
                .favoriteId(checkFavorite.getFavoriteId())
                .member(member)
                .groups(groups)
                .build();
        favoriteRepository.delete(favorite);

        Favorite checkMember = favoriteRepository.findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
        if (null != checkMember) {
            isFavorites = true;
        }

        return ResponseDto.success(
                FavoriteDto.Response.builder()
                        .groupId(favorite.getGroups().getGroupId())
                        .title(favorite.getGroups().getTitle())
                        .imgUrl(favorite.getGroups().getImgUrl())
                        .memberCount(memberCount)
                        .groupTag(tagListByGroup)
                        .favorite(isFavorites)
                        .createdAt(favorite.getGroups().getCreatedAt())
                        .modifiedAt(favorite.getGroups().getModifiedAt())
                        .member(null)
                        .build()
        );
    }
}
