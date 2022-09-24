package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    Optional<Participate> findByGroups_GroupIdAndMember_MemberId(Long groupId, Long memberId);
    int countByGroups_GroupId(Long groupId);
    List<Participate> findAllByGroups(Groups groups);
}
