package org.rustlang.oxide.model;

import org.eclipse.sapphire.modeling.IExecutableModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DelegateImplementation;
import org.eclipse.sapphire.modeling.annotations.FileSystemResourceType;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.MustExist;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.annotations.ValidFileSystemResourceType;
import org.eclipse.sapphire.workspace.WorkspaceRelativePath;
import org.rustlang.oxide.command.RustNewFileOperation;
import org.rustlang.oxide.model.service.RustIdentifierValidationService;

@GenerateImpl
public interface RustSourceFile extends IExecutableModelElement {
    ModelElementType TYPE = new ModelElementType(RustSourceFile.class);

    @Label(standard = "&name")
    @Required
    @Service(impl = RustIdentifierValidationService.class)
    ValueProperty PROP_NAME = new ValueProperty(TYPE, "Name");

    @Type(base = Path.class)
    @Label(standard = "&folder")
    @Required
    @ValidFileSystemResourceType(FileSystemResourceType.FOLDER)
    @MustExist
    @WorkspaceRelativePath
    ValueProperty PROP_FOLDER = new ValueProperty(TYPE, "FOLDER");

    Value<String> getName();

    void setName(String name);

    Value<Path> getFolder();

    void setFolder(Path folder);

    void setFolder(String folder);

    @Override
    @DelegateImplementation(RustNewFileOperation.class)
    Status execute(ProgressMonitor monitor);
}
