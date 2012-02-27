package org.rustlang.oxide.model;

import com.google.common.base.Strings;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelPropertyChangeEvent;
import org.eclipse.sapphire.modeling.ModelPropertyListener;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.services.DefaultValueService;
import org.eclipse.sapphire.services.DefaultValueServiceData;

public class CrateNameDefaultValueService extends DefaultValueService {
    @Override
    protected void initDefaultValueService() {
        final ModelPropertyListener listener = new ModelPropertyListener() {
            @Override
            public void handlePropertyChangedEvent(
                    @SuppressWarnings("unused")
                    final ModelPropertyChangeEvent event) {
                refresh();
            }
        };
        RustProject.PROP_PROJECT_NAME.refine(context(IModelElement.class))
                .addListener(listener);
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
