package org.rustlang.oxide.language.model;

import java.util.UUID;

public class CrateAttributes {
    private final String name;
    private final CrateType type;
    private final UUID uuid;
    private final String version;
    private final String url;
    private final String author;
    private final String licenseName;
    private final String briefDescription;
    private final String longDescription;

    public CrateAttributes(final String name, final CrateType type,
            final String version, final UUID uuid, final String url,
            final String author, final String licenseName,
            final String briefDescription, final String longDescription) {
        this.name = name;
        this.type = type;
        this.version = version;
        this.uuid = uuid;
        this.url = url;
        this.author = author;
        this.licenseName = licenseName;
        this.briefDescription = briefDescription;
        this.longDescription = longDescription;
    }

    public String getName() {
        return name;
    }

    public CrateType getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }
}
