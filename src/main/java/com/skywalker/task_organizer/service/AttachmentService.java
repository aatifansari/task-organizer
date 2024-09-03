package com.skywalker.task_organizer.service;

import com.skywalker.task_organizer.dto.AttachmentsDto;
import com.skywalker.task_organizer.entity.Attachment;
import com.skywalker.task_organizer.exception.EntityNotFoundException;
import com.skywalker.task_organizer.exception.UnauthorizedAccessException;
import com.skywalker.task_organizer.repository.AttachmentRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final FileSystemStorageService storageService;
    private final AttachmentRepository attachmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);

    public AttachmentService(FileSystemStorageService storageService, AttachmentRepository attachmentRepository){
        this.storageService = storageService;
        this.attachmentRepository = attachmentRepository;
    }

    public AttachmentsDto save(MultipartFile file, String loggedInUser){
        LOGGER.info("File uploading initiated {}", file);
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().concat("." + fileExtension);
        Attachment attachment = createEntity(file, filename, loggedInUser);
        storageService.store(file, filename);
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return mapAttachmentToDto(attachment);
    }

    public List<AttachmentsDto> saveAll(List<MultipartFile> files, String loggedInUser){
        List<Attachment> attachments = new ArrayList<>();
        files.forEach(file -> {
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString().concat(fileExtension);
            Attachment attachment = createEntity(file, filename, loggedInUser);
            storageService.store(file, filename);
            attachments.add(attachment);
        });
        List<Attachment> savedAttachments = attachmentRepository.saveAll(attachments);
        return savedAttachments.stream().map(a->mapAttachmentToDto(a)).collect(Collectors.toList());
    }

    public Map<String, String> getById(String attachmentId, String loggedInUser){
        Map<String, String> map = new LinkedHashMap<>();
        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(EntityNotFoundException::new);
        if(!attachment.getCreatedBy().equals(loggedInUser)){
            throw new UnauthorizedAccessException();
        }
        Path path = storageService.load(attachment.getFilename());
        map.put("path", path.toString());
        map.put("content-type", attachment.getContentType());
        map.put("content-size", attachment.getContentSize().toString());
        return map;
    }

    private Attachment createEntity(MultipartFile file, String filename, String loggedInUser){
        return Attachment.builder()
                .id(UUID.randomUUID().toString())
                .filename(filename)
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentSize(file.getSize())
                .createdAt(Instant.now())
                .createdBy(loggedInUser)
                .updatedAt(Instant.now())
                .updatedBy(loggedInUser)
                .build();
    }


    private AttachmentsDto mapAttachmentToDto(Attachment attachments){
        return AttachmentsDto.builder()
                .id(attachments.getId())
                .taskId(attachments.getTask() != null ?attachments.getTask().getId() : null)
                .originalFilename(attachments.getOriginalFilename())
                .build();
    }


}
