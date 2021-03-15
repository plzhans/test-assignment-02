package com.plzhans.assignment.api.service.test.datatype;

import com.plzhans.assignment.common.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    int no;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public TestDto(TestEntity entity){
        this.no = entity.getNo();
        this.name = entity.getName();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
