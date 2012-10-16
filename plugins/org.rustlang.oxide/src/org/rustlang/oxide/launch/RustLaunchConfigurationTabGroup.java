/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.launch;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class RustLaunchConfigurationTabGroup extends
        AbstractLaunchConfigurationTabGroup {
    private final List<ILaunchConfigurationTab> tabs;

    @Inject
    RustLaunchConfigurationTabGroup(final List<ILaunchConfigurationTab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void createTabs(
            @SuppressWarnings("unused") @Nullable
            final ILaunchConfigurationDialog dialog,
            @SuppressWarnings("unused") @Nullable final String mode) {
        setTabs(Iterables.toArray(getConfigurationTabs(),
                ILaunchConfigurationTab.class));
    }

    @SuppressWarnings("null")
    List<ILaunchConfigurationTab> getConfigurationTabs() {
        return tabs;
    }
}
