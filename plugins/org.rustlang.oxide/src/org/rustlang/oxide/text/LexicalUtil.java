package org.rustlang.oxide.text;

public final class LexicalUtil {
    private LexicalUtil() {
    }

    public static boolean isRustIdentifier(final String string) {
        if (string.isEmpty() || !isRustIdentifierStart(string.charAt(0))) {
            return false;
        }
        for (int i = 1; i < string.length(); ++i) {
            if (!isRustIdentifierPart(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRustIdentifierStart(final char c) {
        return Character.isUnicodeIdentifierStart(c) || c == '_';
    }

    public static boolean isRustIdentifierPart(final char c) {
        return Character.isUnicodeIdentifierPart(c) || c == '_';
    }
}
