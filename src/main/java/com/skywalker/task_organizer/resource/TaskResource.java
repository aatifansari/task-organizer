package com.skywalker.task_organizer.resource;

import com.skywalker.task_organizer.dto.CommentDto;
import com.skywalker.task_organizer.dto.TaskDto;
import com.skywalker.task_organizer.entity.TaskStatus;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskResource {

    private final TaskService taskService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResource.class);

    @Autowired
    public TaskResource(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE})
    protected ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal User user){
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.create(taskDto, loggedInUser));
    }

    @GetMapping("/{id}")
    protected ResponseEntity<?> fetchTaskById(@PathVariable(name = "id") String taskId,
                                              @AuthenticationPrincipal User user) {
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.fetchById(taskId, loggedInUser));
    }

    @GetMapping("/{pageNo}/{pageSize}")
    protected ResponseEntity<?> fetchAllTasks(@PathVariable("pageNo") Integer pageNo,
                                              @PathVariable("pageSize") Integer pageSize,
                                              @AuthenticationPrincipal User user){
        String loggedInUser = user.getUserId();
        try{
            return ResponseEntity.ok(taskService.fetchAllByUserId(loggedInUser, pageNo, pageSize));
        }catch (Exception e){
            return new ResponseEntity<>("Unexpected Error Occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    protected ResponseEntity<TaskDto> updateTask(@PathVariable(name = "id") String taskId,
                                                 @RequestBody TaskDto taskDto,
                                                 @AuthenticationPrincipal User user) {
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.update(taskId, taskDto, loggedInUser));

    }

    @PutMapping("/{id}/transition")
    protected ResponseEntity<?> updateTaskStatus(@PathVariable(name = "id") String taskId,
                                                 @RequestParam("taskStatus") TaskStatus taskStatus,
                                                 @AuthenticationPrincipal User user) {
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, taskStatus, loggedInUser));

    }

    @PutMapping("/{id}/comment")
    protected ResponseEntity<?> addComment(@PathVariable(name = "id") String taskId,
                                           @RequestBody List<CommentDto> comments,
                                           @AuthenticationPrincipal User user) {
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.addComment(taskId, comments, loggedInUser));

    }

    @DeleteMapping("{id}")
    protected ResponseEntity<?> deleteTask(@PathVariable(name = "id") String taskId,
                                           @AuthenticationPrincipal User user) {
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(taskService.deleteTask(taskId, loggedInUser));
    }

}
