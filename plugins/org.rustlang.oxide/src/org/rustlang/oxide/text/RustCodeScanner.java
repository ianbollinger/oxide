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

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class RustCodeScanner extends BufferedRuleBasedScanner {
    private static final String[] KEYWORDS = {
        "as", "assert", "bind", "break", "const", "copy", "do", "drop", "else",
        "enum", "extern", "fail", "false", "fn", "for", "if", "impl", "let",
        "log", "loop", "mod", "move", "mut", "priv", "pub", "pure", "ref",
        "return", "self", "static", "struct", "trait", "type", "unsafe", "use",
        "while"
    };
    private static final String SINGLE_QUOTE = "'";
    private static final String DOUBLE_QUOTE = "\"";

    public RustCodeScanner(final RustColorManager manager) {
        setRules(createRules(manager));
    }

    private IRule[] createRules(final RustColorManager manager) {
        final IToken numberRuleToken = createToken(manager,
                RustColorConstants.NUMBER);
        final IToken stringToken = createToken(manager,
                RustColorConstants.STRING);
        final IToken commentToken = createToken(manager,
                RustColorConstants.SINGLE_LINE_COMMENT);
        final IToken identToken = createToken(manager,
                RustColorConstants.DEFAULT);
        final IRule[] rules = new IRule[] {
            new WhitespaceRule(new RustWhitespaceDetector()),
            new RustNumberRule(numberRuleToken),
            createWordRules(manager),
            new WordRule(new RustWordDetector(), identToken),
            new SingleLineRule(DOUBLE_QUOTE, DOUBLE_QUOTE, stringToken, '\\',
                    true, true),
            new SingleLineRule(SINGLE_QUOTE, SINGLE_QUOTE, stringToken, '\\'),
            new EndOfLineRule("//", commentToken),
            new MultiLineRule("/*", "*/", commentToken)
        };
        return rules;
    }

    private WordRule createWordRules(final RustColorManager manager) {
        final IToken wordToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.KEYWORD), null, SWT.BOLD));
        final WordRule wordRule = new WordRule(new RustWordDetector());
        for (final String word : KEYWORDS) {
            wordRule.addWord(word, wordToken);
        }
        return wordRule;
    }

    private Token createToken(final RustColorManager manager, final RGB color) {
        return new Token(new TextAttribute(manager.getColor(color)));
    }
}
