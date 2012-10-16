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

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class RustNumberRule implements IRule {
    private final IToken token;

    public RustNumberRule(final IToken token) {
        this.token = token;
    }

    @Override
    public IToken evaluate(
            @SuppressWarnings("null") final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (!isDecimalDigit(c)) {
            scanner.unread();
            return getUndefinedToken();
        }
        final int base = scanPrefix(scanner, c);
        final int n = scanDigits(scanner, base);
        scanSuffix(scanner, n);
        return getToken();
    }

    private void scanSuffix(final ICharacterScanner scanner,
            final int previous) {
        int c = previous;
        if (c == 'u' || c == 'i') {
            scanIntegerSuffix(scanner);
        } else {
            if (c == '.') {
                c = scanDigits(scanner, 10);
            }
            if (c == 'e' || c == 'E') {
                c = scanExponent(scanner);
            }
            if (c == 'f') {
                scanFloatSuffix(scanner);
            } else {
                scanner.unread();
            }
        }
    }

    private int scanPrefix(final ICharacterScanner scanner, final int c) {
        final int n = scanner.read();
        final int base;
        if (c == '0' && n == 'x') {
            base = 16;
        } else if (c == '0' && n == 'b') {
            base = 2;
        } else {
            scanner.unread();
            base = 10;
        }
        return base;
    }

    private void scanFloatSuffix(final ICharacterScanner scanner) {
        final int c = scanner.read();
        final int n = scanner.read();
        if ((c != '3' || n == '2') && (c != '6' || n != '4')) {
            scanner.unread();
            scanner.unread();
        }
    }

    private int scanDigits(final ICharacterScanner scanner, final int radix) {
        int c;
        do {
            c = scanner.read();
        } while (c == '_'
                || (isHexadecimalDigit(c) && Character.digit(c, radix) != -1));
        return c;
    }

    private void scanIntegerSuffix(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (c != '8') {
            final int n = scanner.read();
            if ((c != '1' || n != '6') && (c != '3' || n != '2')
                    && (c != '6' || n != '4')) {
                scanner.unread();
                scanner.unread();
            }
        }
    }

    private int scanExponent(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (c != '-' || c != '+') {
            scanner.unread();
        }
        return scanDigits(scanner, 10);
    }

    // TODO: move these utility methods elsewhere.
    private static boolean inRange(final int c, final int low, final int high) {
        return low <= c && c <= high;
    }

    private static boolean isHexadecimalDigit(final int c) {
        return inRange(c, '0', '9') || inRange(c, 'a', 'f')
                || inRange(c, 'A', 'F');
    }

    public static boolean isDecimalDigit(final int c) {
        return inRange(c, '0', '9');
    }

    public static boolean isAlpha(final int c) {
        return inRange(c, 'a', 'z') || inRange(c, 'A', 'Z');
    }

    @SuppressWarnings("null")
    private IToken getToken() {
        return token;
    }

    @SuppressWarnings("null")
    private IToken getUndefinedToken() {
        return Token.UNDEFINED;
    }
}
