package org.rustlang.oxide.model;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.ValidationService;
import org.rustlang.oxide.text.LexicalUtil;

public class CrateNameValidationService extends ValidationService {
    @Override
    public Status validate() {
        final Value<?> value = context(IModelElement.class).read(
                context(ValueProperty.class));
        final String string = (String) value.getContent();
        if (LexicalUtil.isRustIdentifier(string)) {
            return Status.createOkStatus();
        }
        return Status.createErrorStatus(string + " is not a valid crate name.");
    }
}
