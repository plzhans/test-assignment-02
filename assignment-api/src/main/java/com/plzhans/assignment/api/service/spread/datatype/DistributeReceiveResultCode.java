package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.plzhans.assignment.common.domain.CodeEnumable;

public enum DistributeReceiveResultCode implements CodeEnumable {

    Received(2001, "Already received"),
    Finished(2002, "Already finished"),
    Expired(2003, "Expired"),

    Error(2000, "Ok"),
    Ok(0, "Ok");
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
