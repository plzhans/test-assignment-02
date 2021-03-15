package com.plzhans.assignment.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Test entity.
 */
@Builder
@Data
@Entity
@Table(name = "t_test")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TestEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    int no;

    /**
     * The Name.
     */
    @Column(name = "name")
    String name;

    /**
     * The Created at.
     */
    @CreatedDate
    @Column(name = "created_at")
    LocalDateTime createdAt;

    /**
     * The Updated at.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}
