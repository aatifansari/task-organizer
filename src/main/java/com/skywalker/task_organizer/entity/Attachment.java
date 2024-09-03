package com.skywalker.task_organizer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {

    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Task task;

    @Column(name="filename")
    private String filename;

    @Column(name="original_filename")
    private String originalFilename;

    @Column(name="content_type")
    private String contentType;

    @Column(name = "content_size")
    private Long contentSize;

    @Column(name="crtd_at")
    private Instant createdAt;

    @Column(name="crtd_by")
    private String createdBy;

    @Column(name="uptd_at")
    private Instant updatedAt;

    @Column(name="uptd_by")
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;
        return id != null && id.equals(((Attachment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
