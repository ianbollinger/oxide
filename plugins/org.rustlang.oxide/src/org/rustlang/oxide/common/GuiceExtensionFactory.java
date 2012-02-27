package org.rustlang.oxide.common;

import java.util.List;
import java.util.Map;
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
import org.eclipse.core.runtime.IExtensionRegistry;
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
            @SuppressWarnings("unused") final String name, final Object data) {
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
        final Class<?> clazz;
        try {
            final Bundle bundle = ContributorFactoryOSGi.resolve(contributor);
            clazz = bundle.loadClass(className);
        } catch (final InvalidRegistryObjectException e) {
            throw newCoreException(e);
        } catch (final ClassNotFoundException e) {
            throw newCoreException(e);
        }
        final Object o = getInjector().getInstance(clazz);
        if (o instanceof IExecutableExtension) {
            final IExecutableExtension extension = (IExecutableExtension) o;
            extension.setInitializationData(configuration, null, null);
        }
        return o;
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
            Injector injector = INJECTORS.get(contributor);
            if (injector == null) {
                final ImmutableList.Builder<Module> modules = ImmutableList
                        .builder();
                modules.add(getOSGiServiceRegistry());
                modules.addAll(getModulesContributedByProject());
                injector = Guice.createInjector(modules.build());
                INJECTORS.put(contributor, injector);
            }
            return injector;
        }
    }

    private Module getOSGiServiceRegistry() {
        final Bundle bundle = ContributorFactoryOSGi.resolve(contributor);
        final BundleContext bundleContext = bundle.getBundleContext();
        final ServiceRegistry registry = EclipseRegistry.eclipseRegistry();
        return Peaberry.osgiModule(bundleContext, registry);
    }

    private List<Module> getModulesContributedByProject() throws CoreException {
        final ImmutableList.Builder<Module> modules = ImmutableList.builder();
        final IExtensionRegistry registry = RegistryFactory.getRegistry();
        final IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(ID);
        for (final IConfigurationElement e : elements) {
            if (contributor.equals(e.getContributor())) {
                modules.add((Module) e.createExecutableExtension("class"));
            }
        }
        return modules.build();
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
