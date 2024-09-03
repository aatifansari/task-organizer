package com.skywalker.task_organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentsDto {

    private String id;
    private String taskId;
    private String originalFilename;

}
