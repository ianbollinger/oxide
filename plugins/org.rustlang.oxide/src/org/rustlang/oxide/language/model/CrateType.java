package org.rustlang.oxide.language.model;

public enum CrateType {
    LIBRARY("lib"),
    BINARY("bin");

    private final String value;

    private CrateType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
