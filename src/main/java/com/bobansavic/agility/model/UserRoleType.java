package com.bobansavic.agility.model;

public enum UserRoleType {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    PM("ROLE_PROJECT_MANAGER");

    private String value;

    UserRoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRoleType forValue(String value) {
        for (UserRoleType r : values()) {
            if (r.value.equals(value)) {
                return r;
            }
        }
        return null;
    }
}
