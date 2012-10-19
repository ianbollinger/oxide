/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.template;

import java.io.IOException;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.rustlang.oxide.common.template.BasicTemplateContext;
import org.rustlang.oxide.common.template.TemplateFileWriter;
import org.rustlang.oxide.common.template.TemplateFileWriterFactory;

/**
 * Default Guice {@link com.google.inject.Module} for Rust template
 * implementations.
 *
 * TODO: list bindings.
 */
public class RustTemplateModule extends AbstractModule {
    // TODO: make sure key is named appropriately.
    private static final String RUST_TEMPLATE_STORE_KEY = "RUST_TEMPLATE_STORE";

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
    TemplateStore provideTemplateStore(final IPreferenceStore preferenceStore) {
        final ContributionTemplateStore store = new ContributionTemplateStore(
                preferenceStore, RUST_TEMPLATE_STORE_KEY);
        try {
            store.load();
        } catch (final IOException e) {
            throw Throwables.propagate(e);
        }
        return store;
    }
}
