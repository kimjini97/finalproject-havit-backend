package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Group;
import com.havit.finalbe.entity.GroupTag;
import com.havit.finalbe.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupTagRepository extends JpaRepository<GroupTag, Long> {
    List<GroupTag> findAllByGroup(Group group);
    List<GroupTag> findAllByTags(Tags tags);
}
