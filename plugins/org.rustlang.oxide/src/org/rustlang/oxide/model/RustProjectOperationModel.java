package org.rustlang.oxide.model;

import org.eclipse.sapphire.modeling.IExecutableModelElement;
import org.eclipse.sapphire.modeling.ImpliedElementProperty;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.annotations.DelegateImplementation;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;

@GenerateImpl
public interface RustProjectOperationModel extends IExecutableModelElement {
    ModelElementType TYPE = new ModelElementType(
            RustProjectOperationModel.class);

    @Type(base = RustProject.class)
    @Label(standard = "rust project")
    ImpliedElementProperty PROP_PROJECT = new ImpliedElementProperty(TYPE,
            "Project");

    RustProject getProject();

    @Override
    @DelegateImplementation(RustNewProjectDelegate.class)
    Status execute(ProgressMonitor monitor);
}
