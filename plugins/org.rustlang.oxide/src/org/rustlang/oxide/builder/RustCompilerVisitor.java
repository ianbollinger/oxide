package org.rustlang.oxide.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.Collections3;
import org.rustlang.oxide.common.EnumPreferenceStore;
import org.rustlang.oxide.preferences.RustPreferenceKey;

public class RustCompilerVisitor extends RustBuilderVisitor {
    private final EnumPreferenceStore preferenceStore;

    public RustCompilerVisitor(final EnumPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
    }

    @Override
    public boolean visit(final IResource resource) throws CoreException {
        // TODO: implement
        return true;
    }

    @Override
    public boolean visit(final IResourceDelta delta) throws CoreException {
        // TODO: filter out non-source files.
        return true;
    }
}
