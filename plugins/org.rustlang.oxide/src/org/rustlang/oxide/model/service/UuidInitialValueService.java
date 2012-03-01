package org.rustlang.oxide.model.service;

import java.util.UUID;
import org.eclipse.sapphire.services.InitialValueService;
import org.eclipse.sapphire.services.InitialValueServiceData;

public class UuidInitialValueService extends InitialValueService {
    @Override
    protected InitialValueServiceData compute() {
        return new InitialValueServiceData(UUID.randomUUID().toString());
    }
}
