package com.plzhans.assignment.common.domain.spread;

import com.plzhans.assignment.common.domain.CodeEnumable;

public enum SpreadState implements CodeEnumable {

    None(0, "None"),
    Active(1, "Active"),
    Expired(2, "Expired");

    int state;
    String name;

    SpreadState(int state, String name) {
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

