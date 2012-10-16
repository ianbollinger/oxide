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

import javax.annotation.Nullable;
import com.google.common.base.Strings;
import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.services.DefaultValueService;
import org.eclipse.sapphire.services.DefaultValueServiceData;
import org.rustlang.oxide.model.RustProject;

public class CrateNameDefaultValueService extends DefaultValueService {
    @Override
    protected void initDefaultValueService() {
        final Listener listener = new Listener() {
            @Override
            public void handle(
                    @SuppressWarnings("unused") @Nullable final Event event) {
                refresh();
            }
        };
        RustProject.PROP_PROJECT_NAME.refine(context(IModelElement.class))
                .attach(listener);
    }

    @Override
    protected DefaultValueServiceData compute() {
        // TODO: refactor method.
        final Value<String> value = context(IModelElement.class).read(
                RustProject.PROP_PROJECT_NAME);
        final String string = Strings.nullToEmpty(value.getContent());
        // TODO: make comparison more general.
        if (string.startsWith("rust-")) {
            return new DefaultValueServiceData(string.substring(5));
        }
        return new DefaultValueServiceData(string);
    }
}
