package com.obss.jcp.sinandogan.agileexpress.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StoryPoint {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    StoryPoint(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
