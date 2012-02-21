package org.rustlang.oxide.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.inject.Inject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class RustColorManager {
    private final Map<RGB, Color> colorTable;

    @Inject
    RustColorManager() {
        this.colorTable = new HashMap<RGB, Color>();
    }

    public void dispose() {
        final Iterator<Color> e = colorTable.values().iterator();
        while (e.hasNext()) {
            e.next().dispose();
        }
    }

    public Color getColor(final RGB rgb) {
        Color color = colorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            colorTable.put(rgb, color);
        }
        return color;
    }
}
