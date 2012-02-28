package org.rustlang.oxide.wizards;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.rustlang.oxide.model.RustNewProjectOperation;
import org.rustlang.oxide.model.RustNewProjectOperationInnerFactory;
import org.rustlang.oxide.model.RustProjectOperationModel;

public class RustWizardModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustNewProjectOperation.class,
                        RustNewProjectOperation.class)
                .build(RustNewProjectOperationInnerFactory.class));
        bindConstant().annotatedWith(ProjectDefinition.class)
                .to("org.rustlang.oxide/ui/Project.sdef!wizard");
    }

    @Provides
    RustProjectOperationModel provideRustProjectOperation() {
        return RustProjectOperationModel.TYPE.instantiate();
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface ProjectDefinition {}
}
