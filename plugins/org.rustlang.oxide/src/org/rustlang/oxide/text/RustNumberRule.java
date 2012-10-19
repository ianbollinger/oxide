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

/**
 * TODO: Document class.
 */
public class RustNumberRule implements IRule {
    private final IToken token;

    RustNumberRule(final IToken token) {
        this.token = token;
    }

    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        if (!LexicalUtil.isDecimalDigit(peek(scanner))) {
            return getUndefinedToken();
        }
        scanNumber(scanner);
        return getToken();
    }

    private void scanNumber(final ICharacterScanner scanner) {
        final int base = scanPrefix(scanner);
        scanDigits(scanner, base);
        scanSuffix(scanner);
    }

    private int scanPrefix(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (c == '0') {
            final int n = scanner.read();
            if (n == 'b') {
                return 2;
            }
            if (n == 'x') {
                return 16;
            }
            scanner.unread();
        }
        return 10;
    }

    private void scanSuffix(final ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == 'u' || c == 'i') {
            scanIntegerSuffix(scanner);
        } else {
            if (c == '.') {
                scanDigits(scanner, 10);
                c = scanner.read();
            }
            if (c == 'e' || c == 'E') {
                scanExponent(scanner);
                c = scanner.read();
            }
            if (c == 'f') {
                scanFloatSuffix(scanner);
            } else {
                scanner.unread();
            }
        }
    }

    private void scanFloatSuffix(final ICharacterScanner scanner) {
        final int c = scanner.read();
        final int n = scanner.read();
        if ((c != '3' || n == '2') && (c != '6' || n != '4')) {
            unread(scanner, 2);
        }
    }

    private void scanDigits(final ICharacterScanner scanner, final int radix) {
        int c;
        do {
            c = scanner.read();
        } while (validDigit(c, radix));
        scanner.unread();
    }

    private boolean validDigit(final int c, final int radix) {
        return c == '_' || (LexicalUtil.isHexadecimalDigit(c)
                && Character.digit(c, radix) != -1);
    }

    private void scanIntegerSuffix(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (c != '8') {
            final int n = scanner.read();
            if ((c != '1' || n != '6') && (c != '3' || n != '2')
                    && (c != '6' || n != '4')) {
                unread(scanner, 2);
            }
        }
    }

    private void scanExponent(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (c != '-' || c != '+') {
            scanner.unread();
        }
        scanDigits(scanner, 10);
    }

    private IToken getToken() {
        return token;
    }

    private IToken getUndefinedToken() {
        return Token.UNDEFINED;
    }

    private int peek(final ICharacterScanner scanner) {
        final int c = scanner.read();
        scanner.unread();
        return c;
    }

    private void unread(final ICharacterScanner scanner, final int n) {
        for (int i = 0; i < n; ++i) {
            scanner.unread();
        }
    }
}
