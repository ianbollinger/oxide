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

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 *
 */
public class RustPartitionScanner extends RuleBasedPartitionScanner {
    private static final String SINGLE_QUOTE = "'";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String COMMENT_END = "*/";

    // TODO: Inject fields, don't do work, and make package-private.
    public RustPartitionScanner() {
        setPredicateRules(createRules());
    }

    private IPredicateRule[] createRules() {
        final IToken string = new Token(RustPartitions.RUST_STRING);
        final IToken character = new Token(RustPartitions.RUST_CHARACTER);
        final IToken rustDoc = new Token(RustPartitions.RUST_DOC);
        final IToken multiLineComment = new Token(
                RustPartitions.RUST_MULTI_LINE_COMMENT);
        final IToken singleLineComment = new Token(
                RustPartitions.RUST_SINGLE_LINE_COMMENT);
        return new IPredicateRule[] {
            new EndOfLineRule("//", singleLineComment),
            new SingleLineRule(DOUBLE_QUOTE, DOUBLE_QUOTE, string, '\\',
                    true, true),
            new SingleLineRule(SINGLE_QUOTE, SINGLE_QUOTE, character,
                    '\\'),
            new EmptyCommentRule(multiLineComment),
            new MultiLineRule("/**", COMMENT_END, rustDoc),
            new MultiLineRule("/*", COMMENT_END, multiLineComment)
        };
    }
}
