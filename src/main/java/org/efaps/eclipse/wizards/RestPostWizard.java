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


import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.efaps.eclipse.rest.RestClient;

public class RestPostWizard extends Wizard implements IImportWizard {

    private RestWizardPage restPage;
    private IStructuredSelection selection;

    public RestPostWizard() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        final String url = this.restPage.getComboUrl().getItem(this.restPage.getComboUrl().getSelectionIndex());
        final List<File> files = new ArrayList<File>();
        final Iterator<?> iter = this.selection.iterator();
        while (iter.hasNext()) {
            final IAdaptable adapt = (IAdaptable) iter.next();
            final IFile file = (IFile) adapt.getAdapter(IFile.class);
            if (file != null && file.isAccessible()) {
                final URI uri = file.getLocationURI();
                final File t = new File(uri);
                files.add(t);
            }

        }
        final RestClient client = new RestClient(url);
        client.init();
        try {
            client.post(files);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(final IWorkbench _workbench,
                     final IStructuredSelection _selection) {
        setWindowTitle("File Import Wizard");
        setNeedsProgressMonitor(true);
        this.restPage = new RestWizardPage("Import File");
        this.selection = _selection;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    @Override
    public void addPages() {
        super.addPages();
        addPage(this.restPage);
    }
}
