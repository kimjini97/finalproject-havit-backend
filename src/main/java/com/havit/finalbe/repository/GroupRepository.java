package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Groups;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {
    Slice<Groups> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Groups> findAllByTitleContainingIgnoreCaseOrMember_NicknameContainingIgnoreCase(String title, String nickname);
    Groups findByGroupId(Long groupId);
}
