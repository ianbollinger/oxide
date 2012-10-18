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

package org.rustlang.oxide.common;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.Status;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.ServiceRegistry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

// TODO: replace.
public final class GuiceExtensionFactory implements IExecutableExtension,
        IExecutableExtensionFactory {
    public static final String ID = "org.rustlang.oxide.modules";
    private static final Map<IContributor, Injector> INJECTORS = Maps
            .newHashMap();
    private IConfigurationElement configuration;
    private IContributor contributor;
    private String className;

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @Nullable final String name, final Object data) {
        configuration = config;
        contributor = config.getContributor();
        className = data instanceof String
                ? (String) data
                : config.getAttribute("id");
    }

    @Override
    public Object create() throws CoreException {
        if (className == null) {
            throw newCoreException(
                    "Configuration is missing class information.");
        }
        final Object o = getInstance();
        return (o instanceof IExecutableExtension)
                ? setInitializationData((IExecutableExtension) o)
                : o;
    }

    private IExecutableExtension setInitializationData(
            final IExecutableExtension extension) throws CoreException {
        extension.setInitializationData(configuration, null, null);
        return extension;
    }

    private Object getInstance() throws CoreException {
        return checkNotNull(getInjector().getInstance(loadClass()));
    }

    private Class<?> loadClass() throws CoreException {
        try {
            return getBundle().loadClass(className);
        } catch (final ClassNotFoundException |
                InvalidRegistryObjectException e) {
            throw newCoreException(e);
        }
    }

    private Bundle getBundle() {
        return ContributorFactoryOSGi.resolve(contributor);
    }

    // TODO: call
    public static void cleanup() {
        synchronized (INJECTORS) {
            for (final IContributor contributor : INJECTORS.keySet()) {
                if (ContributorFactoryOSGi.resolve(contributor) == null) {
                    INJECTORS.remove(contributor);
                }
            }
        }
    }

    private Injector getInjector() throws CoreException {
        synchronized (INJECTORS) {
            final Injector injector = INJECTORS.get(contributor);
            return (injector == null)
                    ? createInjector()
                    : injector;
        }
    }

    private Injector createInjector() throws CoreException {
        final List<Module> modules = ImmutableList
                .<Module>builder()
                .add(getOSGiServiceRegistry())
                .addAll(getModulesContributedByProject())
                .build();
        final Injector injector = Guice.createInjector(modules);
        INJECTORS.put(contributor, injector);
        return injector;
    }

    private Module getOSGiServiceRegistry() {
        final BundleContext bundleContext = getBundle().getBundleContext();
        final ServiceRegistry registry = EclipseRegistry.eclipseRegistry();
        return Peaberry.osgiModule(bundleContext, registry);
    }

    private List<Module> getModulesContributedByProject() throws CoreException {
        final ImmutableList.Builder<Module> modules = ImmutableList.builder();
        for (final IConfigurationElement e : getConfigurationElements()) {
            if (contributor.equals(e.getContributor())) {
                modules.add(createModule(e));
            }
        }
        return modules.build();
    }

    private IConfigurationElement[] getConfigurationElements() {
        return RegistryFactory.getRegistry().getConfigurationElementsFor(ID);
    }

    private Module createModule(
            final IConfigurationElement e) throws CoreException {
        return (Module) e.createExecutableExtension("class");
    }

    CoreException newCoreException(final Throwable throwable) {
        return new CoreException(new Status(IStatus.ERROR,
                contributor.getName(), throwable.getMessage(), throwable));
    }

    CoreException newCoreException(final String message) {
        return new CoreException(new Status(IStatus.ERROR,
                contributor.getName(), message));
    }
}
