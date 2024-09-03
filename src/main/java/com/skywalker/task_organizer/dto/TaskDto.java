package com.skywalker.task_organizer.dto;

import com.skywalker.task_organizer.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private String id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private Date date;
    private List<CommentDto> comments;
    private List<AttachmentsDto> attachmentsDtos;
    private String assignee;
}
