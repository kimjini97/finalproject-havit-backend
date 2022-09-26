package com.havit.finalbe.service;

import com.havit.finalbe.dto.FavoriteDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Favorite;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service

public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ServiceUtil serviceUtil;
    private final GroupService groupService;

    @Transactional
    public ResponseDto<?> favorites(FavoriteDto favoriteDto, HttpServletRequest request) {

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

        Groups groups = groupService.isPresentGroup(favoriteDto.getGroupId());
        if (null == groups) {
            return ResponseDto.fail(GROUP_NOT_FOUND);
        }

        Favorite checkFavorite = favoriteRepository.findByMemberAndGroups(member, groups);

        if (null == checkFavorite) {
            Favorite favorite = Favorite.builder()
                    .member(member)
                    .groups(groups)
                    .build();
            favoriteRepository.save(favorite);

            return ResponseDto.success("즐겨찾기가 완료되었습니다.");
        }

        Favorite favorite = Favorite.builder()
                .favoriteId(checkFavorite.getFavoriteId())
                .member(member)
                .groups(groups)
                .build();
        favoriteRepository.delete(favorite);

        return ResponseDto.success("즐겨찾기가 취소되었습니다.");
    }
}
