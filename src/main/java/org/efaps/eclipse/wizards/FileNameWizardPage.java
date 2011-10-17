/*
 * Copyright 2003 - 2011 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev: 2072 $
 * Last Changed:    $Date: 2009-01-13 12:16:14 -0500 (Tue, 13 Jan 2009) $
 * Last Changed By: $Author: jmox $
 */
package org.efaps.eclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FileNameWizardPage
    extends WizardPage
{

    private Composite container;

    private Text text;

    protected FileNameWizardPage(final String _pageName)
    {
        super(_pageName);
    }

    public String getFileNames()
    {
        return this.text.getText();
    }

    public void createControl(final Composite _parent)
    {
        this.container = new Composite(_parent, SWT.NULL);
        final GridLayout layout = new GridLayout(1, false);
        this.container.setLayout(layout);

        final Label label1 = new Label(this.container, SWT.NULL);
        label1.setText("Filenames");

        this.text = new Text(this.container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessVerticalSpace = true;

        this.text.setLayoutData(gridData);
        setControl(this.container);
        setPageComplete(true);
    }
}
