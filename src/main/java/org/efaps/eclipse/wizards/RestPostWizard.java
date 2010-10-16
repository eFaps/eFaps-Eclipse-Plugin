/*
 * Copyright 2003 - 2010 The eFaps Team
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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.rest.RestClient;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: RestClient.java 5691 2010-10-15 22:43:57Z jan.moxter $
 */
public class RestPostWizard
    extends Wizard
    implements IImportWizard
{

    private RestWizardPage restPage;
    private IStructuredSelection selection;

    public RestPostWizard()
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
        final List<File> files = new ArrayList<File>();
        final Iterator<?> iter = this.selection.iterator();
        try {
            while (iter.hasNext()) {
                final IAdaptable adapt = (IAdaptable) iter.next();
                final IResource file;
                if (adapt instanceof IJavaElement) {
                    final IJavaElement comp = (IJavaElement) adapt.getAdapter(IJavaElement.class);
                    file = comp.getCorrespondingResource();
                } else {
                    file = (IResource) adapt.getAdapter(IResource.class);
                }
                if (file != null && file.isAccessible()) {
                    final URI uri = file.getLocationURI();
                    final File t = new File(uri);
                    files.add(t);
                }
            }
            if (!files.isEmpty()) {
                final RestClient client = new RestClient(url);
                client.init();
                client.post(files);
            }
        } catch (final Exception e) {
            EfapsPlugin.getDefault().logError(getClass(), "Exception", e);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(final IWorkbench _workbench,
                     final IStructuredSelection _selection)
    {
        setWindowTitle("File Import Wizard");
        setNeedsProgressMonitor(true);
        this.restPage = new RestWizardPage("Import File");
        this.selection = _selection;
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
    }
}
