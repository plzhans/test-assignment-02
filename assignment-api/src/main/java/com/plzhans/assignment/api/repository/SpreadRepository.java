package com.plzhans.assignment.api.repository;

import com.plzhans.assignment.common.entity.SpreadEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpreadRepository extends JpaRepository<SpreadEventEntity, Integer> {
    SpreadEventEntity findByTokenAndRoomId(String token, String roomId);
}
