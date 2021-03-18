//package com.plzhans.assignment.api.service.spread.datatype;
//
//import com.plzhans.assignment.common.entity.CacheSpreadEventEntity;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//
//public class MemberDto {
//    int no;
//    String name;
//    LocalDateTime createdAt;
//    LocalDateTime updatedAt;
//
//    @Builder
//    public MemberDto() {
//    }
//
//    public MemberDto(CacheSpreadEventEntity entity) {
//        this.no = entity.getNo();
//        this.name = entity.getName();
//        this.createdAt = entity.getCreatedAt();
//        this.updatedAt = entity.getUpdatedAt();
//    }
//}
