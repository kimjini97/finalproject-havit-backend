package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Certify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertifyRepository extends JpaRepository<Certify, Long> {
    List<Certify> findByGroups_GroupIdOrderByCreatedAtDesc(Long groupId);
}
