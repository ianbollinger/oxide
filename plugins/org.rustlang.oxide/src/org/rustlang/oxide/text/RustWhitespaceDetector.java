package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class RustWhitespaceDetector implements IWhitespaceDetector {
    @Override
    public boolean isWhitespace(final char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
}
