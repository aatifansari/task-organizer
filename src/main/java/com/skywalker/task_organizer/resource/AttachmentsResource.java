package com.skywalker.task_organizer.resource;

import com.skywalker.task_organizer.dto.AttachmentsDto;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attachment")
public class AttachmentsResource {

    private final AttachmentService attachmentService;

    public AttachmentsResource(AttachmentService attachmentService){
        this.attachmentService = attachmentService;
    }

    @PostMapping(value = "/upload", consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    protected ResponseEntity<AttachmentsDto> upload(@RequestPart MultipartFile attachment,
                                                    @AuthenticationPrincipal User user){
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(attachmentService.save(attachment, loggedInUser));
    }

    @PostMapping(value = "/upload/all", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    protected ResponseEntity<List<AttachmentsDto>> uploadAll(
            @RequestPart List<MultipartFile> attachments,
            @AuthenticationPrincipal User user){
        String loggedInUser = user.getUserId();
        return ResponseEntity.ok(attachmentService.saveAll(attachments, loggedInUser));
    }

    @GetMapping("/download/{id}")
    protected ResponseEntity<?> download(@PathVariable("id") String attachmentId,
                                         @AuthenticationPrincipal User user){
        String loggedInUser = user.getUserId();
        Map<String, String> fileAttrMap = attachmentService.getById(attachmentId, loggedInUser);
        ByteArrayResource resource;
        try{
            resource = new ByteArrayResource(Files.readAllBytes(Path.of(fileAttrMap.get("path"))));
        }catch(IOException e){
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(Long.parseLong(fileAttrMap.get("content-size")))
                .body(resource);
    }

}
