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

package org.rustlang.oxide.wizard;

import java.util.UUID;
import javax.annotation.Nullable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.rustlang.oxide.OxidePlugin;

/**
 * Default Guice {@link com.google.inject.Module} for Rust wizard
 * implementations.
 *
 * Provides bindings for:
 * TODO: list bindings.
 */
public class WizardModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustProjectWizardPage.class,
                        RustProjectWizardPage.class)
                .build(RustProjectWizardPageFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RustProjectPropertiesGroup.class,
                        RustProjectPropertiesGroup.class)
                .build(RustProjectPropertiesGroupFactory.class));
    }

    @Provides
    WizardNewProjectReferencePage provideReferencedProjectsPage() {
        final WizardNewProjectReferencePage page =
                new WizardNewProjectReferencePage("rustReferenceProjectPage");
        page.setTitle("Rust Project");
        page.setDescription("Select referenced projects.");
        return page;
    }

    @Provides @Nullable
    ImageDescriptor provideImageDescriptor() {
        return OxidePlugin.getImageDescriptor("icons/rust-logo-64x64.png");
    }

    @Provides
    String provideInitialAuthor() {
        return System.getProperty("user.name");
    }

    @Provides
    UUID provideInitialUUID() {
        return UUID.randomUUID();
    }
}
