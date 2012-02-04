package org.rustlang.oxide.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.osgi.service.prefs.Preferences;
import org.rustlang.oxide.OxidePlugin;

public class RustPreferencesInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        final Preferences node = DefaultScope.INSTANCE.getNode(
                OxidePlugin.PLUGIN_ID);
    }
}
