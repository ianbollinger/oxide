package org.rustlang.oxide.text;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public interface IColorManager {
    Color getColor(String key);

    Color getColor(RGB rgb);

    void dispose();

    void bindColor(String key, RGB rgb);

    void unbindColor(String key);
}
