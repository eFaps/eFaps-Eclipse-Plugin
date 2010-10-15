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
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.efaps.eclipse.wizards.CompileWizard;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class CompileHandler
    extends AbstractHandler
{

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    public Object execute(final ExecutionEvent _event)
        throws ExecutionException
    {
        final Shell shell = HandlerUtil.getActiveShell(_event);
        final CompileWizard wiz = new CompileWizard();
        wiz.init(HandlerUtil.getActiveWorkbenchWindow(_event).getWorkbench(), new TreeSelection());
        new WizardDialog(shell, wiz).open();
        return null;
    }
}
