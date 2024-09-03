package com.skywalker.task_organizer.repository;

import com.skywalker.task_organizer.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
