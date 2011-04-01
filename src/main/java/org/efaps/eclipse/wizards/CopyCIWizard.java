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
 * Revision:        $Rev: 5696 $
 * Last Changed:    $Date: 2010-10-15 19:56:28 -0500 (Fri, 15 Oct 2010) $
 * Last Changed By: $Author: jan.moxter $
 */

package org.efaps.eclipse.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class CopyCIWizard
    extends Wizard
    implements IImportWizard
{

    private CopyCIWizardPage copyCIPage;


    @Override
    public boolean performFinish()
    {
        final IFile file = this.copyCIPage.createNewFile();
        return file != null;
    }

    public void init(final IWorkbench _workbench,
                     final IStructuredSelection _selection)
    {
        setWindowTitle("Copy Configuration Item Wizard");
        this.copyCIPage = new CopyCIWizardPage("Import File", _selection);
    }

    @Override
    public void addPages()
    {
        super.addPages();
        addPage(this.copyCIPage);
    }
}
