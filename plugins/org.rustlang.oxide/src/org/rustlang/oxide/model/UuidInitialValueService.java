package org.rustlang.oxide.model;

import java.util.UUID;
import org.eclipse.sapphire.services.InitialValueService;
import org.eclipse.sapphire.services.InitialValueServiceData;

public class UuidInitialValueService extends InitialValueService {
    @Override
    protected void initInitialValueService() {
    }

    @Override
    protected InitialValueServiceData compute() {
        // TODO: cache value?
        return new InitialValueServiceData(UUID.randomUUID().toString());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
