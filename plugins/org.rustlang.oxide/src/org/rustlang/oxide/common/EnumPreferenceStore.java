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

package org.rustlang.oxide.common;

import com.google.inject.Inject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class EnumPreferenceStore {
    private final IPreferenceStore store;

    @Inject
    EnumPreferenceStore(final IPreferenceStore store) {
        this.store = store;
    }

    public void addPropertyChangeListener(
            final IPropertyChangeListener listener) {
        store.addPropertyChangeListener(listener);
    }

    public boolean contains(final PreferenceKey key) {
        return store.contains(key.toString());
    }

    public <T> void firePropertyChangeEvent(final PreferenceKey key,
            final T oldValue, final T newValue) {
        store.firePropertyChangeEvent(key.toString(), oldValue, newValue);
    }

    public boolean getBoolean(final PreferenceKey key) {
        return store.getBoolean(key.toString());
    }

    public boolean getDefaultBoolean(final PreferenceKey key) {
        return store.getDefaultBoolean(key.toString());
    }

    public double getDefaultDouble(final PreferenceKey key) {
        return store.getDefaultDouble(key.toString());
    }

    public float getDefaultFloat(final PreferenceKey key) {
        return store.getDefaultFloat(key.toString());
    }

    public int getDefaultInt(final PreferenceKey key) {
        return store.getDefaultInt(key.toString());
    }

    public long getDefaultLong(final PreferenceKey key) {
        return store.getDefaultLong(key.toString());
    }

    public String getDefaultString(final PreferenceKey key) {
        return store.getDefaultString(key.toString());
    }

    public double getDouble(final PreferenceKey key) {
        return store.getDefaultDouble(key.toString());
    }

    public float getFloat(final PreferenceKey key) {
        return store.getDefaultFloat(key.toString());
    }

    public int getInt(final PreferenceKey key) {
        return store.getDefaultInt(key.toString());
    }

    public long getLong(final PreferenceKey key) {
        return store.getDefaultLong(key.toString());
    }

    public String getString(final PreferenceKey key) {
        return store.getString(key.toString());
    }

    public boolean isDefault(final PreferenceKey key) {
        return store.isDefault(key.toString());
    }

    public boolean needsSaving() {
        return store.needsSaving();
    }

    public void putValue(final PreferenceKey key, final String value) {
        store.putValue(key.toString(), value);
    }

    public void removePropertyChangeListener(
            final IPropertyChangeListener listener) {
        store.removePropertyChangeListener(listener);
    }

    public void setDefault(final PreferenceKey key, final double value) {
        store.setDefault(key.toString(), value);
    }

    public void setDefault(final PreferenceKey key, final float value) {
        store.setDefault(key.toString(), value);
    }

    public void setDefault(final PreferenceKey key, final int value) {
        store.setDefault(key.toString(), value);
    }

    public void setDefault(final PreferenceKey key, final long value) {
        store.setDefault(key.toString(), value);
    }

    public void setDefault(final PreferenceKey key, final String value) {
        store.setDefault(key.toString(), value);
    }

    public void setDefault(final PreferenceKey key, final boolean value) {
        store.setDefault(key.toString(), value);
    }

    public void setToDefault(final PreferenceKey key) {
        store.setToDefault(key.toString());
    }

    public void setValue(final PreferenceKey key, final double value) {
        store.setValue(key.toString(), value);
    }

    public void setValue(final PreferenceKey key, final float value) {
        store.setValue(key.toString(), value);
    }

    public void setValue(final PreferenceKey key, final int value) {
        store.setValue(key.toString(), value);
    }

    public void setValue(final PreferenceKey key, final long value) {
        store.setValue(key.toString(), value);
    }

    public void setValue(final PreferenceKey key, final String value) {
        store.setValue(key.toString(), value);
    }

    public void setValue(final PreferenceKey key, final boolean value) {
        store.setValue(key.toString(), value);
    }
}
