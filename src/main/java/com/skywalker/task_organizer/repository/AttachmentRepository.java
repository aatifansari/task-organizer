package com.skywalker.task_organizer.repository;

import com.skywalker.task_organizer.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
