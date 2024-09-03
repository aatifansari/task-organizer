package com.skywalker.task_organizer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Comment {
    @Id
    private String id;

    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Task task;

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
        if (!(o instanceof Comment )) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
