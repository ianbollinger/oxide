package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.*;

public abstract class SingleCharacterRule implements IRule {
    private final IToken token;

    public SingleCharacterRule(final IToken token) {
        this.token = token;
    }

    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        if (isRuleCharacter(scanner.read())) {
            return token;
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    protected abstract boolean isRuleCharacter(int c);
}
