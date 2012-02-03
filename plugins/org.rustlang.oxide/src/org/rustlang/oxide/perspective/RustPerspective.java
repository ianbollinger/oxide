package org.rustlang.oxide.perspective;

import org.eclipse.ui.*;
import org.rustlang.oxide.wizards.RustProjectWizard;

public class RustPerspective implements IPerspectiveFactory {
    private static final String SEARCH_VIEW_ID =
            "org.eclipse.search.ui.views.SearchView";
    private static final String CONSOLE_VIEW_ID =
            "org.eclipse.ui.console.ConsoleView";
    // TODO RustNavigatorView
    @SuppressWarnings("deprecation")
    private static final String NAVIGATOR_ID = IPageLayout.ID_RES_NAV;

    private static final String[] WIZARD_SHORTCUTS = new String[] {
        RustProjectWizard.WIZARD_ID,
        // TODO RustSourceFolderWizard
        // TODO new .rs file
        // TODO new .rc file
        "org.eclipse.ui.wizards.new.folder",
        "org.eclipse.ui.wizards.new.file",
        "org.eclipse.ui.editors.wizards.UntitledTextFileWizard"
    };

    private static final String[] VIEW_SHORTCUTS = new String[] {
        SEARCH_VIEW_ID,
        CONSOLE_VIEW_ID,
        IPageLayout.ID_OUTLINE,
        IPageLayout.ID_PROBLEM_VIEW,
        NAVIGATOR_ID,
        "org.eclipse.pde.runtime.LogView",
        IPageLayout.ID_TASK_LIST
    };

    private static final String[] BOTTOM_FOLDER_PLACEHOLDERS = new String[] {
        SEARCH_VIEW_ID,
        CONSOLE_VIEW_ID,
        IPageLayout.ID_BOOKMARKS,
        IPageLayout.ID_PROGRESS_VIEW
    };

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        defineWizardShortcuts(layout);
        defineViewShortcuts(layout);
        defineLayout(layout);
    }

    private void defineWizardShortcuts(final IPageLayout layout) {
        for (final String shortcut : WIZARD_SHORTCUTS) {
            layout.addNewWizardShortcut(shortcut);
        }
    }

    private void defineViewShortcuts(final IPageLayout layout) {
        for (final String shortcut : VIEW_SHORTCUTS) {
            layout.addShowViewShortcut(shortcut);
        }
    }

    private void defineLayout(final IPageLayout layout) {
        createLeftFolder(layout);
        createBottomFolder(layout);
        createRightFolder(layout);
    }

    private void createLeftFolder(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        final float ratio = 0.25f;
        layout.addView(NAVIGATOR_ID, IPageLayout.LEFT, ratio, editorArea);
    }

    private void createBottomFolder(final IPageLayout layout) {
        final IFolderLayout bottom = layout.createFolder("bottom",
                IPageLayout.BOTTOM, 0.75f, layout.getEditorArea());
        bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
        for (final String placeholder : BOTTOM_FOLDER_PLACEHOLDERS) {
            bottom.addPlaceholder(placeholder);
        }
    }

    private void createRightFolder(final IPageLayout layout) {
        final float ratio = 0.75f;
        layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, ratio,
                layout.getEditorArea());
    }
}
