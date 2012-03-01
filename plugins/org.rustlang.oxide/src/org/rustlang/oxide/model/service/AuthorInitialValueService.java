package org.rustlang.oxide.model.service;

import org.eclipse.sapphire.services.InitialValueService;
import org.eclipse.sapphire.services.InitialValueServiceData;

public class AuthorInitialValueService extends InitialValueService {
    @Override
    protected InitialValueServiceData compute() {
        return new InitialValueServiceData(System.getProperty("user.name"));
    }
}
