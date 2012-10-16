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

package org.rustlang.oxide.model;

import org.eclipse.sapphire.modeling.IExecutableModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DelegateImplementation;
import org.eclipse.sapphire.modeling.annotations.FileSystemResourceType;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.MustExist;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.annotations.ValidFileSystemResourceType;
import org.eclipse.sapphire.workspace.WorkspaceRelativePath;
import org.rustlang.oxide.command.RustNewFileOperation;
import org.rustlang.oxide.model.service.RustIdentifierValidationService;

@GenerateImpl
public interface RustSourceFile extends IExecutableModelElement {
    ModelElementType TYPE = new ModelElementType(RustSourceFile.class);

    @Label(standard = "&name")
    @Required
    @Service(impl = RustIdentifierValidationService.class)
    ValueProperty PROP_NAME = new ValueProperty(TYPE, "Name");

    @Type(base = Path.class)
    @Label(standard = "&folder")
    @Required
    @ValidFileSystemResourceType(FileSystemResourceType.FOLDER)
    @MustExist
    @WorkspaceRelativePath
    ValueProperty PROP_FOLDER = new ValueProperty(TYPE, "FOLDER");

    Value<String> getName();

    void setName(String name);

    Value<Path> getFolder();

    void setFolder(Path folder);

    void setFolder(String folder);

    @Override
    @DelegateImplementation(RustNewFileOperation.class)
    Status execute(@SuppressWarnings("null") ProgressMonitor monitor);
}
