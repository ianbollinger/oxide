package org.rustlang.oxide.model.service;

import java.util.UUID;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;
import org.eclipse.sapphire.services.ValueSerializationService;

public class UuidSerializationService extends ValueSerializationService {
    @Override
    protected Object decodeFromString(final String value) {
        try {
            return UUID.fromString(value);
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    public static class Factory extends ServiceFactory {
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
            return new UuidSerializationService();
        }
    }
}
