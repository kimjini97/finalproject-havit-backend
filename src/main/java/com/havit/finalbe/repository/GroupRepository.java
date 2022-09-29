package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {
    List<Groups> findAllByOrderByCreatedAtDesc();
    List<Groups> findAllByTitleContainingIgnoreCaseOrMember_NicknameContainingIgnoreCase(String title, String nickname);
    List<Groups> findAllByMember_MemberId(Long memberId);
    Groups findByGroupId(Long groupId);
}
