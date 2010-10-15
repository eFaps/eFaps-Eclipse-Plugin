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
package org.efaps.eclipse.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.efaps.eclipse.wizards.RestPostWizard;

/**
 * @author The eFaps Team
 * @version $Id: SLF4JOverEclipseConsole.java 2072 2009-01-13 17:16:14Z jmox $
 */
public class RestPostAction
    implements IObjectActionDelegate
{

    /**
     * Shell used by this Action.
     */
    private Shell shell;

    /**
     * Selection that will contain the Files.
     */
    private TreeSelection selection;

    /**
     * Constructor for Action.
     */
    public RestPostAction()
    {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(final IAction action,
                              final IWorkbenchPart targetPart)
    {
        this.shell = targetPart.getSite().getShell();
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(final IAction action)
    {
        final RestPostWizard wiz = new RestPostWizard();
        wiz.init( PlatformUI.getWorkbench(), this.selection);
        new WizardDialog(this.shell, wiz).open();
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(final IAction _action,
                                 final ISelection _selection)
    {
        this.selection = (TreeSelection) _selection;
    }
}
