package com.skywalker.task_organizer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "address")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @Column(name = "id")
    private String addressId;

    private String location;

    private String country;

    private String state;

    private String city;

    private String pincode;

    private boolean isDeleted;

    @Column(name="crtd_at")
    private Instant createdAt;

    @Column(name="crtd_by")
    private String createdBy;

    @Column(name="uptd_at")
    private Instant updatedAt;

    @Column(name="uptd_by")
    private String updatedBy;

}