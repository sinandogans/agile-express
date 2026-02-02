package com.obss.jcp.sinandogan.agileexpress.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    MANAGER("manager"),
    TEAM_LEAD("team_lead"),
    MEMBER("team_member"),
    ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);
    }
}
