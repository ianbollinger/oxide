package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * TODO: Document class.
 */
public class EmptyCommentDetector implements IWordDetector {
    EmptyCommentDetector() {
    }

    @Override
    public boolean isWordStart(final char c) {
        return c == '/';
    }

    @Override
    public boolean isWordPart(final char c) {
        return c == '*' || c == '/';
    }
}
