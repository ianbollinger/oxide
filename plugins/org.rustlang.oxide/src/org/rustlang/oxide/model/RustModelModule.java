package org.rustlang.oxide.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

public class RustModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(ProjectDefinition.class)
                .to("org.rustlang.oxide/ui/Project.sdef!wizard");
        bindConstant().annotatedWith(SourceFileDefinition.class)
                .to("org.rustlang.oxide/ui/RustSourceFile.sdef!wizard");
    }

    @Provides
    RustSourceFile provideRustSourceFile() {
        return RustSourceFile.TYPE.instantiate();
    }

    @Provides
    RustProjectOperationModel provideRustProjectOperation() {
        return RustProjectOperationModel.TYPE.instantiate();
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface ProjectDefinition {}

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface SourceFileDefinition {}
}
