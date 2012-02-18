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

public class RustCodeScanner extends BufferedRuleBasedScanner {
    private static final String[] KEYWORDS = {
        "alt", "as", "assert", "be", "bind", "break", "check", "claim", "const",
        "cont", "copy", "do", "else", "enum", "export", "fail", "false", "fn",
        "for", "if", "iface", "impl", "import", "in", "let", "log", "mod",
        "move", "mutable", "native", "of", "pure", "resource", "ret", "self",
        "to", "type", "unchecked", "unsafe", "use", "while", "with",
        // TYPES
        "bool", "char", "f32", "f64", "float", "i16", "i32", "i64", "i8",
        "int", "str", "u16", "u32", "u64", "u8", "uint", "vec"
    };

    public RustCodeScanner(final RustColorManager manager) {
        final IToken numberRuleToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.NUMBER)));
        final IToken stringToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.STRING)));
        final IToken commentToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.SINGLE_LINE_COMMENT)));
        final IToken wordToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.KEYWORD), null, SWT.BOLD));
        final IToken identToken = new Token(new TextAttribute(
                manager.getColor(RustColorConstants.DEFAULT)));

        final WordRule wordRule = new WordRule(new RustWordDetector());
        for (final String word : KEYWORDS) {
            wordRule.addWord(word, wordToken);
        }

        final IRule[] rules = new IRule[] {
            new WhitespaceRule(new RustWhitespaceDetector()),
            new RustNumberRule(numberRuleToken), wordRule,
            new WordRule(new RustWordDetector(), identToken),
            new SingleLineRule("\"", "\"", stringToken, '\\', true, true),
            new SingleLineRule("'", "'", stringToken, '\\'),
            new EndOfLineRule("//", commentToken),
            new MultiLineRule("/*", "*/", commentToken)
        };
        setRules(rules);
    }
}
