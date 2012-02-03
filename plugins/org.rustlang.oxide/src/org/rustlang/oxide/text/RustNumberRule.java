package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.*;

public class RustNumberRule implements IRule {
    private final IToken token;

    public RustNumberRule(final IToken token) {
        this.token = token;
    }

    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        final int c = scanner.read();
        if (!isDecimalDigit(c)) {
            scanner.unread();
            return Token.UNDEFINED;
        }
        final int base = scanPrefix(scanner, c);
        final int n = scanDigits(scanner, base);
        scanSuffix(scanner, n);
        return token;
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
        if (c == '0' && n == 'x') {
            return 16;
        }
        if (c == '0' && n == 'b') {
            return 2;
        }
        scanner.unread();
        return 10;
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
}
