package org.rustlang.oxide.text;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class RustPartitionScanner extends RuleBasedPartitionScanner {
    public RustPartitionScanner() {
        setPredicateRules(new IPredicateRule[] {
        });
    }
}
