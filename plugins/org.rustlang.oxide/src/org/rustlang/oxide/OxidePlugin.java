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

package org.rustlang.oxide;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nullable;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rustlang.oxide.common.CommonModule;
import org.rustlang.oxide.common.EnumPreferenceStore;
import org.slf4j.Logger;

// TODO: eliminate circular dependencies.
public class OxidePlugin extends AbstractUIPlugin {
    public static final String ID = "org.rustlang.oxide";
    private static OxidePlugin plugin;
    private Injector injector;

    @Override
    public void start(@Nullable final BundleContext context) throws Exception {
        super.start(context);
        injector = Guice.createInjector(new OxideModule(), new CommonModule());
        plugin = this;
    }

    @Override
    public void stop(@Nullable final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static OxidePlugin getDefault() {
        return checkNotNull(plugin);
    }

    public static ImageDescriptor getImageDescriptor(
            final String imageFilePath) {
        return imageDescriptorFromPlugin(ID, imageFilePath);
    }

    public static Logger getLogger() {
        return getInstance(Logger.class);
    }

    public static EnumPreferenceStore getEnumPreferenceStore() {
        return getInstance(EnumPreferenceStore.class);
    }

    public static <T> T getInstance(final Class<T> clazz) {
        return plugin.injector.getInstance(clazz);
    }
}
