package org.rustlang.oxide.model;

import org.eclipse.sapphire.modeling.annotations.Label;

@Label(standard = "type", full = "crate type")
public enum CrateType {
    @Label(standard = "library")
    LIBRARY("lib"),

    @Label(standard = "binary")
    BINARY("bin");

    private final String value;

    private CrateType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
