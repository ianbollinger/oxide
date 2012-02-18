package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IToken;

public class RustBraceRule extends SingleCharacterRule {
    public RustBraceRule(final IToken token) {
        super(token);
    }

    @Override
    protected boolean isRuleCharacter(final int c) {
        return c == '(' || c == ')' || c == '<' || c == '>' || c == '['
                || c == ']' || c == '{' || c == '}';
    }
}
