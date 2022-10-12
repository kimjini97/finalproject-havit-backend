package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CertifyRepository extends JpaRepository<Certify, Long> {
    List<Certify> findByGroups_GroupIdOrderByCreatedAtDesc(Long groupId);
    List<Certify> findByCreatedDateAndMember_MemberIdAndGroups_GroupId(LocalDate yesterday, Long memberId, Long groupId);
    List<Certify> findAllByMember_MemberId(Long memberId);
}