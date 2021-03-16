package com.plzhans.assignment.api.service.common.datatype;

import com.plzhans.assignment.common.domain.CodeEnumable;

public enum ErrorCode implements CodeEnumable {

    InvalidParams(1001,"invalid params"),

    Error(1, "Error"),
    Ok(0, "Ok");

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getInt() {
        return this.code;
    }

    @Override
    public String getString() {
        return this.message;
    }
}
