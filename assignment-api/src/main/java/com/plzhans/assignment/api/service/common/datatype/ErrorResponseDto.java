package com.plzhans.assignment.api.service.common.datatype;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    int code;
    String message;

    public ErrorResponseDto(ErrorCode code){
        this.code = code.getInt();
        this.message = code.getString();
    }
}
