package org.rustlang.oxide.model;

import java.net.URL;
import java.util.UUID;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.AbsolutePath;
import org.eclipse.sapphire.modeling.annotations.ClearOnDisable;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.Enablement;
import org.eclipse.sapphire.modeling.annotations.FileSystemResourceType;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.InitialValue;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.MustExist;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.annotations.ValidFileSystemResourceType;

// TODO: standard formatting rules make this interface harder to maintain.
// TODO: extract crate model?
// TODO: fix formatting of capitalized type names.
// TODO: add accelerators for all fields.
@GenerateImpl
public interface RustProject extends IModelElement {
    ModelElementType TYPE = new ModelElementType(RustProject.class);

    @Label(standard = "&project name")
    @Required
    ValueProperty PROP_PROJECT_NAME = new ValueProperty(TYPE, "ProjectName");

    @Type(base = Boolean.class)
    @Label(standard = "use &default location")
    @DefaultValue(text = "true")
    ValueProperty PROP_USE_DEFAULT_LOCATION = new ValueProperty(TYPE,
            "UseDefaultLocation");

    // TODO: browse should start in workspace.
    @Label(standard = "&location", full = "project location directory")
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

    // TODO: implement working sets.
    @Label(standard = "w&orking sets")
    @Enablement(expr = "false")
    ValueProperty PROP_WORKING_SETS = new ValueProperty(TYPE,
            "WorkingSets");

    // TODO: automatically deduce name if project begins with rust prefix.
    @Label(standard = "crate name")
    @DefaultValue(text = "${ProjectName}")
    @Required
    ValueProperty PROP_CRATE_NAME = new ValueProperty(TYPE, "CrateName");

    @Type(base = CrateType.class)
    @Label(standard = "type")
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
    @Label(standard = "URL")
    @Required
    ValueProperty PROP_URL = new ValueProperty(TYPE, "Url");

    @Label(standard = "&author")
    @Service(impl = AuthorInitialValueService.class)
    ValueProperty PROP_AUTHOR = new ValueProperty(TYPE, "Author");

    @Label(standard = "&license")
    ValueProperty PROP_LICENSE = new ValueProperty(TYPE, "License");

    @Label(standard = "brief", full = "brief description")
    ValueProperty PROP_BRIEF = new ValueProperty(TYPE, "Brief");

    // TODO: fix vertical scaling.
    @Label(standard = "description", full = "long description")
    @LongString
    ValueProperty PROP_DESCRIPTION = new ValueProperty(TYPE, "Description");

    Value<String> getProjectName();

    void setProjectName(String name);

    Value<Boolean> getUseDefaultLocation();

    void setUseDefaultLocation(Boolean useDefaultLocation);

    void setUseDefaultLocation(String useDefaultLocation);

    Value<Path> getProjectLocation();

    void setProjectLocation(Path location);

    void setProjectLocation(String location);

    Value<Boolean> getAddProjectToWorkingSets();

    void setAddProjectToWorkingSets(Boolean addProjectToWorkingSets);

    void setAddProjectToWorkingSets(String addProjectToWorkingSets);

    Value<String> getWorkingSets();

    void setWorkingSets(String workingSets);

    Value<String> getCrateName();

    void setCrateName(String name);

    Value<CrateType> getType();

    void setType(String type);

    void setType(CrateType type);

    Value<String> getVersion();

    void setVersion(String version);

    Value<UUID> getUuid();

    void setUuid(UUID uuid);

    void setUuid(String uuid);

    Value<URL> getUrl();

    void setUrl(String url);

    void setUrl(URL url);

    Value<String> getAuthor();

    void setAuthor(String author);

    Value<String> getLicense();

    void setLicense(String license);

    Value<String> getBrief();

    void setBrief(String brief);

    Value<String> getDescription();

    void setDescription(String author);
}
