package org.rustlang.oxide.model;

import java.util.UUID;
import org.eclipse.sapphire.services.ValueSerializationService;

public class UuidValueSerializationService extends ValueSerializationService {
    @Override
    protected Object decodeFromString(final String value) {
        return UUID.fromString(value);
    }
}
