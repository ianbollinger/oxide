package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IToken;

public class RustOperatorRule extends SingleCharacterRule {
    public RustOperatorRule(final IToken token) {
        super(token);
    }

    @Override
    public boolean isRuleCharacter(final int c) {
        return c == ';' || c == '.' || c == ':' || c == '=' || c == '-'
                || c == '+' || c == '\\' || c == '*' || c == '!' || c == '%'
                || c == '^' || c == '&' || c == '~' || c == '>' || c == '<'
                || c == '|' || c == '/' || c == ',' || c == '#' || c == '@';
    }
}
