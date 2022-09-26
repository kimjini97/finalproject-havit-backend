package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Favorite;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByMemberAndGroups(Member member, Groups groups);
    Favorite findByMember_MemberIdAndGroups_GroupId(Long memberId, Long groupId);
}
