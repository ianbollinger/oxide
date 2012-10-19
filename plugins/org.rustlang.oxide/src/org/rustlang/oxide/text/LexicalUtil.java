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

package org.rustlang.oxide.text;

/**
 * TODO: Document class.
 */
public final class LexicalUtil {
    private LexicalUtil() {
    }

    static boolean isRustIdentifier(final String string) {
        return hasValidIdentifierStart(string)
                && hasValidIdentifierBody(string);
    }

    static boolean isRustIdentifierStart(final char c) {
        return Character.isUnicodeIdentifierStart(c) || c == '_';
    }

    static boolean isRustIdentifierPart(final char c) {
        return Character.isUnicodeIdentifierPart(c) || c == '_';
    }

    static boolean isHexadecimalDigit(final int c) {
        return inRange(c, '0', '9') || inRange(c, 'a', 'f')
                || inRange(c, 'A', 'F');
    }

    static boolean isDecimalDigit(final int c) {
        return inRange(c, '0', '9');
    }

    static boolean isAlpha(final int c) {
        return inRange(c, 'a', 'z') || inRange(c, 'A', 'Z');
    }

    private static boolean hasValidIdentifierStart(final String string) {
        return !string.isEmpty() && isRustIdentifierStart(string.charAt(0));
    }

    private static boolean hasValidIdentifierBody(final String string) {
        for (int i = 1; i < string.length(); ++i) {
            if (!isRustIdentifierPart(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean inRange(final int c, final int low, final int high) {
        return low <= c && c <= high;
    }
}
