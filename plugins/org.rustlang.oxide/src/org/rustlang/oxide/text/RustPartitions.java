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
public final class RustPartitions {
    /**
     * TODO: Document field.
     */
    public static final String RUST_PARTITIONING = "__rust_partitioning";

    // TODO: make a list.
    /**
     * TODO: Document field.
     */
    public static final String[] CONTENT_TYPES = new String[] {
        RustPartitions.RUST_DOC,
        RustPartitions.RUST_MULTI_LINE_COMMENT,
        RustPartitions.RUST_SINGLE_LINE_COMMENT,
        RustPartitions.RUST_STRING,
        RustPartitions.RUST_CHARACTER
    };

    static final String RUST_DOC = "__rust_doc";
    static final String RUST_SINGLE_LINE_COMMENT =
            "__rust_singleline_comment";
    static final String RUST_MULTI_LINE_COMMENT =
            "__rust_multiline_comment";
    static final String RUST_STRING = "__rust_string";
    static final String RUST_CHARACTER = "__rust_character";

    private RustPartitions() {
    }
}
