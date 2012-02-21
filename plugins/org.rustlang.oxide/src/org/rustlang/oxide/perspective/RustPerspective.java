package org.rustlang.oxide.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

// TODO: eliminate this class altogether if possible.
public class RustPerspective implements IPerspectiveFactory {
    @Override
    public void createInitialLayout(
            @SuppressWarnings("unused") final IPageLayout layout) {
        // Everything is defined in the plugin.xml.
    }
}
