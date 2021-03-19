package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.plzhans.assignment.common.domain.CodeEnumable;

public enum DistributeReceiveResultCode implements CodeEnumable {

    Received(1, "Received"),
    Finished(2, "Finished"),

    None(0, "None");
    int code;
    String message;

    DistributeReceiveResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    @JsonValue
    @Override
    public int getInt() {
        return this.code;
    }

    @Override
    public String getString() {
        return this.message;
    }
}
