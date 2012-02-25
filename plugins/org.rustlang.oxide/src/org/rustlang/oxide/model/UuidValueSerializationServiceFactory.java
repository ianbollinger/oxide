package org.rustlang.oxide.model;

import java.util.UUID;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

public class UuidValueSerializationServiceFactory extends ServiceFactory {
    @Override
    public boolean applicable(final ServiceContext context,
            @SuppressWarnings("unused")
            final Class<? extends Service> service) {
        final ValueProperty property = context.find(ValueProperty.class);
        return property != null && property.isOfType(UUID.class);
    }

    @Override
    public Service create(
            @SuppressWarnings("unused") final ServiceContext context,
            @SuppressWarnings("unused")
            final Class<? extends Service> service) {
        // TODO: inject
        return new UuidValueSerializationService();
    }
}
