package org.rustlang.oxide.text;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

/**
 * TODO: Document class.
 */
public class SingleTokenRustScanner extends RuleBasedScanner {
    private final RustColorManager colorManager;

    SingleTokenRustScanner(final RustColorManager colorManager,
            final RGB color) {
        this.colorManager = colorManager;
        // TODO: don't do work inside constructor.
        setDefaultReturnToken(createToken(color));
    }

    // TODO: encapsulate this logic elsewhere.
    private Token createToken(final RGB color) {
        return new Token(new TextAttribute(colorManager.getColor(color)));
    }
}
