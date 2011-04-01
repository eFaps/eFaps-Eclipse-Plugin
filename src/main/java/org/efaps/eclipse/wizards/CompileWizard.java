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
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.eclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.efaps.eclipse.rest.RestClient;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class CompileWizard
    extends Wizard
    implements IImportWizard
{

    private RestWizardPage restPage;
    private CompileWizardPage compilePage;

    public CompileWizard()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {

        final String url = this.restPage.getComboUrl().getItem(this.restPage.getComboUrl().getSelectionIndex());
        final RestClient client = new RestClient(url);
        client.init();
        if (this.compilePage.getCheckJasper().getSelection()) {
            client.compile("jasper");
        }
        if (this.compilePage.getCheckJava().getSelection()) {
            client.compile("java");
        }
        if (this.compilePage.getCheckCss().getSelection()) {
            client.compile("css");
        }
        if (this.compilePage.getCheckJavaScript().getSelection()) {
            client.compile("js");
        }
        if (this.compilePage.getCheckWiki().getSelection()) {
            client.compile("wiki");
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(final IWorkbench workbench,
                     final IStructuredSelection selection)
    {
        setWindowTitle("Compile Wizard"); // NON-NLS-1
        setNeedsProgressMonitor(true);
        this.restPage = new RestWizardPage("Import File"); // NON-NLS-1
        this.compilePage = new CompileWizardPage("Target");
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    @Override
    public void addPages()
    {
        super.addPages();
        addPage(this.restPage);
        addPage(this.compilePage);
    }

}
