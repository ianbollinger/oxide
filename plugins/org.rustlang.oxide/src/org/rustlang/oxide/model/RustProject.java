/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.model;

import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;

// TODO: standard formatting rules make this interface harder to maintain.
// TODO: extract crate model?
// TODO: fix formatting of capitalized type names.

/*
 @Label(standard = "&project name")
 @Required
 ValueProperty PROP_PROJECT_NAME = new ValueProperty(TYPE, "ProjectName");

 @Type(base = Boolean.class)
 @Label(standard = "use &default location")
 @DefaultValue(text = "true")
 ValueProperty PROP_USE_DEFAULT_LOCATION = new ValueProperty(TYPE,
 "UseDefaultLocation");

 // TODO: browse should start in workspace.
 @Label(standard = "&location", full = "project &location directory")
 @Type(base = Path.class)
 @AbsolutePath
 @ValidFileSystemResourceType(FileSystemResourceType.FOLDER)
 @MustExist
 @Enablement(expr = "${!UseDefaultLocation}")
 @ClearOnDisable
 @Required
 ValueProperty PROP_PROJECT_LOCATION = new ValueProperty(TYPE,
 "ProjectLocation");

 @Type(base = Boolean.class)
 @Label(standard = "add projec&t to working sets")
 ValueProperty PROP_ADD_PROJECT_TO_WORKING_SETS = new ValueProperty(TYPE,
 "AddProjectToWorkingSets");

 @Label(standard = "w&orking sets")
 @Enablement(expr = "false")
 ValueProperty PROP_WORKING_SETS = new ValueProperty(TYPE, "WorkingSets");

 @Services({ @Service(impl = CrateNameDefaultValueService.class),
 @Service(impl = RustIdentifierValidationService.class) })
 @Label(standard = "&crate name")
 @Required
 ValueProperty PROP_CRATE_NAME = new ValueProperty(TYPE, "CrateName");

 @Type(base = CrateType.class)
 @Label(standard = "t&ype", full = "crate t&ype")
 @InitialValue(text = "BINARY")
 @Required
 ValueProperty PROP_TYPE = new ValueProperty(TYPE, "Type");

 @Label(standard = "&version")
 @InitialValue(text = "0.0.1")
 @Required
 ValueProperty PROP_VERSION = new ValueProperty(TYPE, "Version");

 // TODO: add action for re-generating UUID.
 @Type(base = UUID.class)
 @Label(standard = "&UUID")
 @Service(impl = UuidInitialValueService.class)
 @Required
 ValueProperty PROP_UUID = new ValueProperty(TYPE, "Uuid");

 @Type(base = URL.class)
 @Label(standard = "U&RL")
 @Required
 ValueProperty PROP_URL = new ValueProperty(TYPE, "Url");

 @Label(standard = "&author")
 @Service(impl = AuthorInitialValueService.class)
 ValueProperty PROP_AUTHOR = new ValueProperty(TYPE, "Author");

 @Label(standard = "l&icense")
 // TODO: get licenses from appropriate location.
 @PossibleValues(values = {"Apache-2.0", "BSD-2-clause", "MIT", "GPL-3.0",
 "LGPL-3.0", "OTHER"})
 @InitialValue(text = "MIT")
 ValueProperty PROP_LICENSE = new ValueProperty(TYPE, "License");

 @Label(standard = "bri&ef", full = "bri&ef description")
 ValueProperty PROP_BRIEF = new ValueProperty(TYPE, "Brief");

 // TODO: fix vertical scaling.
 @Label(standard = "lo&ng", full = "lo&ng description")
 @LongString
 ValueProperty PROP_DESCRIPTION = new ValueProperty(TYPE, "Description");
 */

public class RustProject {
    private final String author;
    private final String brief;
    private String crateName;
    private final String description;
    private final String license;
    private Path projectLocation;
    private final String projectName;
    private final CrateType type;
    private final String url;
    private final UUID uuid;
    private final String version;
    private boolean addProjectToWorkingSets;
    private boolean useDefaultLocation;
    private String workingSets;

    public RustProject(final String projectName, final CrateType type,
            final String version, final UUID uuid, final String url,
            final String author, final String license, final String brief,
            final String description) {
        this.projectName = projectName;
        this.type = type;
        this.version = version;
        this.uuid = uuid;
        this.url = url;
        this.author = author;
        this.license = license;
        this.brief = brief;
        this.description = description;
    }

    public boolean getAddProjectToWorkingSets() {
        return addProjectToWorkingSets;
    }

    public String getAuthor() {
        return author;
    }

    public String getBrief() {
        return brief;
    }

    public String getCrateName() {
        return crateName;
    }

    public String getDescription() {
        return description;
    }

    public String getLicense() {
        return license;
    }

    public Path getProjectLocation() {
        return projectLocation;
    }

    public String getProjectName() {
        return projectName;
    }

    public CrateType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public boolean getUseDefaultLocation() {
        return useDefaultLocation;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getVersion() {
        return version;
    }

    public String getWorkingSets() {
        return workingSets;
    }
}
