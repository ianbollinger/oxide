package org.rustlang.oxide.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.util.Util;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.EnumPreferenceStore;

public class RustPreferencesInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        final EnumPreferenceStore store = OxidePlugin.getEnumPreferenceStore();
        store.setDefault(RustPreferenceKey.COMPILER_PATH,
                getDefaultCompilerName());
    }

    private String getDefaultCompilerName() {
        System.getenv("PATH");
        return getExecutableName("rustc");
    }

    private String getExecutableName(final String name) {
        return name + (Util.isWindows() ? ".exe" : "");
    }
}
