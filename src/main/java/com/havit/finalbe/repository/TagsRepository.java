package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tags, Long> {
    Tags findByTagName(String tagName);
}
