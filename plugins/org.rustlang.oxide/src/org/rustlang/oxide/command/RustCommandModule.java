package org.rustlang.oxide.command;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class RustCommandModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustNewProjectOperation.class,
                        RustNewProjectOperation.class)
                .build(RustNewProjectOperationInnerFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RustNewFileOperation.class,
                        RustNewFileOperation.class)
                .build(RustNewFileOperationFactory.class));
    }
}
