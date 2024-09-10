package com.skywalker.task_organizer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 256, message = "Title must be from 3 to 256  characters.")
    private String title;

    @Size(max = 500, message = "First Name must be less than 500 characters.")
    private String description;

    private TaskStatus taskStatus;

    @NotNull(message = "Date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @OneToMany(mappedBy="task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Attachment> attachments;

    @OneToMany(mappedBy="task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

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
