package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Object> findByUsername(String username);
    Member findMemberByMemberId(Long memberId);
}
