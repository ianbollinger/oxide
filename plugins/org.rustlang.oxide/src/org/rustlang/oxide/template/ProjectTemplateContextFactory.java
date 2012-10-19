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

package org.rustlang.oxide.template;

import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateContext;
import org.rustlang.oxide.common.template.BasicTemplateContext;
import org.rustlang.oxide.model.RustProject;

/**
 * TODO: Document class.
 */
public class ProjectTemplateContextFactory {
    private final BasicTemplateContext context;

    @Inject
    ProjectTemplateContextFactory(final BasicTemplateContext context) {
        this.context = context;
    }

    /**
     * TODO: Document method.
     *
     * @param project
     * @return
     */
    public TemplateContext create(final RustProject project) {
        context.setVariable("project_name", project.getProjectName());
        context.setVariable("crate_type", project.getType().toString());
        context.setVariable("version", project.getVersion());
        context.setVariable("uuid", project.getUuid().toString());
        context.setVariable("url", project.getUrl().toString());
        context.setVariable("author", project.getAuthor());
        context.setVariable("license", project.getLicense());
        context.setVariable("brief", project.getBrief());
        context.setVariable("desc", project.getDescription());
        return context;
    }
}
