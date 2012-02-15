package org.rustlang.oxide.perspective;

import org.eclipse.ui.*;

public class RustPerspective implements IPerspectiveFactory {
    @Override
    public void createInitialLayout(
            @SuppressWarnings("unused") final IPageLayout layout) {
        // Everything is created in the plugin.xml.
    }
}
