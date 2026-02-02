package com.obss.jcp.sinandogan.agileexpress.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    UNASSIGNED(1),
    ASSIGNED(2),
    IN_PROGRESS(3),
    DONE(4);

    private final int value;

    TaskStatus(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
