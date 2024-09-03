package com.skywalker.task_organizer.repository;

import com.skywalker.task_organizer.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    Page<Task> findAllByAssigneeOrCreatedByAndIsDeleted(String loggedInUser, String createdBy, Boolean isDeleted, Pageable pageable);
    Optional<Task> findByIdAndIsDeleted(String taskId, Boolean isDeleted);
    Optional<Task> findByIdAndCreatedByAndIsDeleted(String taskId, String loggedInUser, Boolean isDeleted);
}
