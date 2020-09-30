package com.bobansavic.agility.model;

import java.util.ArrayList;
import java.util.List;

public enum TicketStatus {
    BACKLOG ("Backlog"),
    IN_PROGRESS ("In progress"),
    TESTING ("Testing"),
    DONE ("Done");

    private String value;

    TicketStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> stringValues() {
        List<String> stringValues = new ArrayList<>();
        for (TicketStatus ts : values()) {
            stringValues.add(ts.getValue());
        }
        return stringValues;
    }

    public static TicketStatus forValue(String value) {
        for (TicketStatus ts : values()) {
            if (ts.value.equals(value)) {
                return ts;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
