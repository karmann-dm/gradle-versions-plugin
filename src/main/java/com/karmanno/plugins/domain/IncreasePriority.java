package com.karmanno.plugins.domain;

public enum IncreasePriority {
    MAJOR(4),
    MINOR(3),
    PATCH(2),
    BUILD(1),
    NO_PRIORITY(0);

    private final int value;

    IncreasePriority(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
