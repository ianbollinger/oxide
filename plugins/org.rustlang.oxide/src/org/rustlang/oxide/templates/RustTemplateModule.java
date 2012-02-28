package org.rustlang.oxide.templates;

import java.io.IOException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.rustlang.oxide.common.EclipseLogger;

public class RustTemplateModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TemplateContext.class).to(BasicTemplateContext.class);
        bind(TemplateContextType.class).to(RustTemplateContextType.class);
        install(new FactoryModuleBuilder()
                .implement(TemplateFileWriter.class,
                        TemplateFileWriter.class)
                .build(TemplateFileWriterFactory.class));
    }

    @Provides @Singleton
    TemplateStore provideTemplateStore(final EclipseLogger logger,
            final IPreferenceStore preferenceStore) {
        // TODO: store string constant somewhere, and make sure it's named
        // appropriately.
        final ContributionTemplateStore store = new ContributionTemplateStore(
                preferenceStore, "RUST_TEMPLATE_STORE");
        try {
            store.load();
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
        return store;
    }
}
