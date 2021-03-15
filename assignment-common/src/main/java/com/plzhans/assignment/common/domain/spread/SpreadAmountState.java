package com.plzhans.assignment.common.domain.spread;

import com.plzhans.assignment.common.domain.CodeEnumable;

public enum SpreadAmountState implements CodeEnumable {

    None(0, "None"),
    Ready(1, "Ready"),
    Received(2, "Received");

    int state;
    String name;

    SpreadAmountState(int state, String name) {
        this.state = state;
        this.name = name;
    }

    @Override
    public int getInt() {
        return this.state;
    }

    @Override
    public String getString() {
        return this.name;
    }
}

