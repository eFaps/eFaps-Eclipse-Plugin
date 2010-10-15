/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.efaps.eclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.efaps.eclipse.rest.RestClient;

public class CompileWizard extends Wizard implements IImportWizard {

    private RestWizardPage restPage;
    private CompileWizardPage compilePage;

    public CompileWizard() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

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
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        setWindowTitle("File Import Wizard"); //NON-NLS-1
        setNeedsProgressMonitor(true);
        this.restPage = new RestWizardPage("Import File"); //NON-NLS-1
        this.compilePage = new CompileWizardPage("Target");
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    @Override
    public void addPages() {
        super.addPages();
        addPage(this.restPage);
        addPage(this.compilePage);
    }

}
