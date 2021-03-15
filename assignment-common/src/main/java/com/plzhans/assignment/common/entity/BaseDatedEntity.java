package com.plzhans.assignment.common.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseDatedEntity extends BaseEntity {
    @Getter
    @CreatedDate
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Getter
    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
