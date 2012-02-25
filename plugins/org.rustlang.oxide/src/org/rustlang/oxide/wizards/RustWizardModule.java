package org.rustlang.oxide.wizards;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.model.RustCreateProjectOperation;
import org.rustlang.oxide.model.RustCreateProjectOperationFactory;
import org.rustlang.oxide.model.RustProjectOperation;

public class RustWizardModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustCreateProjectOperation.class,
                        RustCreateProjectOperation.class)
                .build(RustCreateProjectOperationFactory.class));
        bindConstant().annotatedWith(ProjectDefinition.class)
                .to("org.rustlang.oxide/ui/Project.sdef!wizard");
    }

    @Provides
    RustProjectOperation provideRustProjectOperation() {
        return RustProjectOperation.TYPE.instantiate();
    }

    // TODO: use this again.
    @Provides
    ImageDescriptor provideImageDescriptor() {
        // TODO: inject plug-in.
        return OxidePlugin.getImageDescriptor("icons/rust-logo-64x64.png");
    }

    // TODO: use this again.
    @Provides
    List<String> provideLicenseNames() {
        // TODO: get this list from somewhere appropriate.
        return ImmutableList.of("Apache-2.0", "BSD-2-clause", "MIT", "GPL-3.0",
                "LGPL-3.0");
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface Licenses {}

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface ProjectDefinition {}
}
