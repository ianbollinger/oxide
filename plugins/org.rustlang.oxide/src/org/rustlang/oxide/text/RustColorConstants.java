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

import org.eclipse.swt.graphics.RGB;

public final class RustColorConstants {
    public static final RGB MULTI_LINE_COMMENT = new RGB(63, 127, 95);
    public static final RGB SINGLE_LINE_COMMENT = new RGB(63, 127, 95);
    public static final RGB NUMBER = new RGB(42, 0, 255);
    public static final RGB STRING = new RGB(42, 0, 255);
    public static final RGB DEFAULT = new RGB(0, 0, 0);
    public static final RGB TAG = new RGB(0, 0, 128);
    public static final RGB KEYWORD = new RGB(127, 0, 85);
    public static final RGB PUNCTUATION = new RGB(0, 0, 0);
    public static final RGB BRACKET = new RGB(0, 0, 0);

    private RustColorConstants() {
    }
}
