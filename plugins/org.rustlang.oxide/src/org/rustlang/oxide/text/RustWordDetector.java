package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IWordDetector;

public class RustWordDetector implements IWordDetector {
    @Override
    public boolean isWordStart(final char c) {
        return LexicalUtil.isRustIdentifierStart(c);
    }

    @Override
    public boolean isWordPart(final char c) {
        return LexicalUtil.isRustIdentifierPart(c);
    }
}
