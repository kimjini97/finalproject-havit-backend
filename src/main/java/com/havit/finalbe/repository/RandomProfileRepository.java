package com.havit.finalbe.repository;

import com.havit.finalbe.entity.RandomProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RandomProfileRepository extends JpaRepository<RandomProfile, Long> {
}
