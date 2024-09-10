package com.skywalker.task_organizer.dto;

import com.skywalker.task_organizer.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private String id;

    @NotBlank(message = "Title ie required")
    @Size(min = 3, max = 256, message = "Title must be from 3 to 256  characters.")
    private String title;

    @Size(max = 500, message = "First Name must be less than 500 characters.")
    private String description;

    private TaskStatus taskStatus;

    @NotNull(message = "Date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private List<CommentDto> comments;

    private List<AttachmentsDto> attachmentsDtos;

    private String assignee;
}
