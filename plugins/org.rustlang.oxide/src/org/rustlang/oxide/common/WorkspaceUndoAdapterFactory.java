package org.rustlang.oxide.common;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

public class WorkspaceUndoAdapterFactory {
    public IAdaptable create(final Shell shell) {
        return WorkspaceUndoUtil.getUIInfoAdapter(shell);
    }
}
