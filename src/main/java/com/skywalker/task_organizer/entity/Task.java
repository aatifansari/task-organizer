package com.skywalker.task_organizer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private Date date;
    @OneToMany(mappedBy="task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Attachment> attachments;

    @OneToMany(mappedBy="task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comment> comments;
    private String assignee;
    private boolean isDeleted;
    @Column(name="crtd_at")
    private Instant createdAt;
    @Column(name="crtd_by")
    private String createdBy;
    @Column(name="uptd_at")
    private Instant updatedAt;
    @Column(name="uptd_by")
    private String updatedBy;

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setTask(null);
    }

    public void addAttachments(Attachment attachment){
        attachments.add(attachment);
        attachment.setTask(this);
    }

    public void removeAttachments(Attachment attachment){
        attachments.remove(attachment);
        attachment.setTask(null);

    }

}
