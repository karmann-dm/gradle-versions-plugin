package com.karmanno.plugins;

public enum IncreasePriority {
    MAJOR(4),
    MINOR(3),
    PATCH(2),
    BUILD(1);

    private final int value;

    IncreasePriority(final int value) {
        this.value = value;
    }
}
