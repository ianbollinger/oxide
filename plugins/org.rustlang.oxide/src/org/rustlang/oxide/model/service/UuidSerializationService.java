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

package org.rustlang.oxide.model.service;

import java.util.UUID;
import javax.annotation.Nullable;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;
import org.eclipse.sapphire.services.ValueSerializationService;

public class UuidSerializationService extends ValueSerializationService {
    @Override
    @Nullable
    protected Object decodeFromString(
            @SuppressWarnings("null") final String value) {
        try {
            final UUID string = UUID.fromString(value);
            assert string != null;
            return string;
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    public static class Factory extends ServiceFactory {
        @Override
        public boolean applicable(
                @SuppressWarnings("null") final ServiceContext context,
                @SuppressWarnings("unused") @Nullable
                final Class<? extends Service> service) {
            final ValueProperty property = context.find(ValueProperty.class);
            return property != null && property.isOfType(UUID.class);
        }

        @Override
        public Service create(
                @SuppressWarnings("unused") @Nullable
                final ServiceContext context,
                @SuppressWarnings("unused") @Nullable
                final Class<? extends Service> service) {
            return new UuidSerializationService();
        }
    }
}
