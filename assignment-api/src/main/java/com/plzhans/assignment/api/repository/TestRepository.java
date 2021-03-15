package com.plzhans.assignment.api.repository;

import com.plzhans.assignment.common.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer> {
    List<TestEntity> findAll();
}
