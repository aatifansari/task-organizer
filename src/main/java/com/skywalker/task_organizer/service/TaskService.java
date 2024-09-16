package com.skywalker.task_organizer.service;

import com.skywalker.task_organizer.dto.AttachmentsDto;
import com.skywalker.task_organizer.dto.CommentDto;
import com.skywalker.task_organizer.dto.TaskDto;
import com.skywalker.task_organizer.entity.*;
import com.skywalker.task_organizer.exception.BadRequestException;
import com.skywalker.task_organizer.exception.EntityNotFoundException;
import com.skywalker.task_organizer.exception.UnauthorizedAccessException;
import com.skywalker.task_organizer.repository.AttachmentRepository;
import com.skywalker.task_organizer.repository.CommentRepository;
import com.skywalker.task_organizer.repository.TaskRepository;
import com.skywalker.task_organizer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service


public class TaskService {

    private final StorageService storageService;
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(StorageService storageService, TaskRepository taskRepository,
                       AttachmentRepository attachmentRepository, CommentRepository commentRepository,
                       UserRepository userRepository){
        this.storageService = storageService;
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskDto create(TaskDto taskDto, String loggedInUser){
        // validate assignee
//        User assignee = null;
//        if(taskDto.getAssignee() != null){
//            assignee = userRepository.getReferenceById(taskDto)  findById(taskDto.getAssignee())
//                    .orElseThrow(() -> new EntityNotFoundException("Invalid Assignee"));
//        }else{
//            assignee = new User();
//            assignee.setUserId(loggedInUser);
//        }
        Task task = mapDtoToTask(taskDto, loggedInUser);
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setCreatedBy(loggedInUser);
        Set<Attachment> attachments = new HashSet<>();
        Optional.ofNullable(taskDto.getAttachmentsDtos())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .forEach(attachmentDto -> {
                    Attachment attachment = attachmentRepository.findById(attachmentDto.getId()).orElseThrow(EntityNotFoundException::new);
                    attachment.setTask(task);
                    attachments.add(attachment);
                });

        Set<Comment> comments = Optional.ofNullable(taskDto.getComments())
                .orElse(Collections.emptyList())
                .stream()
                .map(s-> mapDtoToComment(s.getComment(), taskDto.getAssignee(), task))
                .collect(Collectors.toSet());
//        task.setComments(comments);
//        task.setAttachments(attachments);
        Task savedTask = taskRepository.save(task);
        return mapTaskToDto(savedTask);

    }

    public TaskDto fetchById(String id, String loggedInUser){
        Task task = taskRepository.findByIdAndIsDeleted(id, false).orElseThrow(EntityNotFoundException::new);
        if(!loggedInUser.equals(task.getCreatedBy()) && !loggedInUser.equals(task.getAssignee())){
            throw new UnauthorizedAccessException("");
        }
        return buildTaskDto(task);
    }

    public Map<String, Object> fetchAllByUserId(String loggedInUser, Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> pageableTasks = taskRepository.findAllByAssigneeOrCreatedByAndIsDeleted(loggedInUser, loggedInUser, false, pageable);
        pageableTasks.map(t->buildTaskDto(t));
        Map<String, Object> response = new HashMap<>();
        response.put("data", pageableTasks.get());
        response.put("totalEntries", pageableTasks.getTotalElements());
        response.put("pageNo", pageNo);
        return response;
    }

    public TaskDto update(String taskId, TaskDto taskDto, String loggedInUser){
        if(taskId == null){
            throw new BadRequestException();
        }
        Task task = taskRepository.findByIdAndIsDeleted(taskId, false).orElseThrow(EntityNotFoundException::new);
        if(!loggedInUser.equals(task.getCreatedBy())){
            throw new UnauthorizedAccessException();
        }
        task = mapDtoToTask(taskDto, loggedInUser);
        Task savedTask = taskRepository.save(task);
        return buildTaskDto(savedTask);
    }

    public TaskDto updateTaskStatus(String taskId, TaskStatus taskStatus, String loggedInUser){
        if(taskId == null){
            throw new BadRequestException();
        }
        Task task = taskRepository.findByIdAndIsDeleted(taskId, false).orElseThrow(EntityNotFoundException::new);
        if(!loggedInUser.equals(task.getCreatedBy()) && !loggedInUser.equals(task.getAssignee())){
            throw new UnauthorizedAccessException();
        }
        task.setTaskStatus(taskStatus);
        task.setUpdatedAt(Instant.now());
        task.setUpdatedBy(loggedInUser);
        Task savedTask = taskRepository.saveAndFlush(task);
        return buildTaskDto(savedTask);
    }

    public String addComment(String taskId, List<CommentDto> comments, String loggedInUser){
        if(taskId == null){
            throw new BadRequestException();
        }
        Task task = taskRepository.findByIdAndIsDeleted(taskId, false).orElseThrow(EntityNotFoundException::new);
        if(!loggedInUser.equals(task.getCreatedBy()) && !loggedInUser.equals(task.getAssignee())){
            throw new UnauthorizedAccessException();
        }
        Set<Comment> existingComments = task.getComments();
        comments.stream()
                .map(comm->mapDtoToComment(comm.getComment(), loggedInUser, task))
                .forEach(comm -> {
                            existingComments.add(comm);
                        });
        task.setComments(existingComments);
        task.setUpdatedAt(Instant.now());
        task.setUpdatedBy(loggedInUser);
        taskRepository.save(task);
        return "Comment Updated Successfully.";
    }

    public String deleteTask(String taskId, String loggedInUser){
         Task task = taskRepository.findByIdAndCreatedByAndIsDeleted(taskId, loggedInUser, false).orElseThrow(EntityNotFoundException::new);
         task.setDeleted(true);
         taskRepository.save(task);
         return "Task Deleted Successfully";
    }

    private TaskDto buildTaskDto(Task task){
        TaskDto taskDto = mapTaskToDto(task);
        List<CommentDto> comments = task.getComments().stream().map(c->mapCommentToDto(c)).collect(Collectors.toList());
        List<AttachmentsDto> attachmentsDtos = task.getAttachments().stream()
                .map(attach -> mapAttachmentToDto(attach)).collect(Collectors.toList());
        taskDto.setComments(comments);
        taskDto.setAttachmentsDtos(attachmentsDtos);
        return taskDto;
    }

    private Task mapDtoToTask(TaskDto taskDto, String loggedInUser) {

        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .taskStatus(taskDto.getTaskStatus() != null ? taskDto.getTaskStatus() : TaskStatus.STARTED)
                .assignee(userRepository.getReferenceById(taskDto.getAssignee() != null ? taskDto.getAssignee() : loggedInUser))
                .date(taskDto.getDate())
                .isDeleted(false)
                .updatedAt(Instant.now())
                .updatedBy(loggedInUser)
                .build();
    }

    private TaskDto mapTaskToDto(Task task){
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskStatus(task.getTaskStatus())
                .date(task.getDate())
                .assignee(task.getAssignee().getUserId())
                .build();
    }

    private Comment mapDtoToComment(String comment, String userId, Task task){
        return Comment.builder()
                .id(UUID.randomUUID().toString())
                .comment(comment)
                .task(task)
                .createdAt(Instant.now())
                .createdBy(userId)
                .updatedAt(Instant.now())
                .updatedBy(userId)
                .build();
    }

    private CommentDto mapCommentToDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .taskId(comment.getTask().getId())
                .build();
    }

    private AttachmentsDto mapAttachmentToDto(Attachment attachments){
        return AttachmentsDto.builder()
                .id(attachments.getId())
                .taskId(attachments.getTask().getId())
                .originalFilename(attachments.getOriginalFilename())
                .build();
    }


}
