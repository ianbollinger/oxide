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
import org.rustlang.oxide.model.RustNewProjectDelegate;
import org.rustlang.oxide.model.RustNewProjectDelegateFactory;
import org.rustlang.oxide.model.RustNewProjectOperation;
import org.rustlang.oxide.model.RustNewProjectOperationFactory;
import org.rustlang.oxide.model.RustProjectOperationModel;

public class RustWizardModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustNewProjectDelegate.class,
                        RustNewProjectDelegate.class)
                .build(RustNewProjectDelegateFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RustNewProjectOperation.class,
                        RustNewProjectOperation.class)
                .build(RustNewProjectOperationFactory.class));
        bindConstant().annotatedWith(ProjectDefinition.class)
                .to("org.rustlang.oxide/ui/Project.sdef!wizard");
    }

    @Provides
    RustProjectOperationModel provideRustProjectOperation() {
        return RustProjectOperationModel.TYPE.instantiate();
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
