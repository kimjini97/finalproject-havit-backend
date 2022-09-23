package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    List<SubComment> findAllByComment(Comment comment);
}
