package org.rustlang.oxide.common;

import com.google.inject.AbstractModule;
import org.rustlang.oxide.common.SubProgressMonitorFactory.WorkScale;

public class CommonModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(WorkScale.class).to(1000);
    }
}
