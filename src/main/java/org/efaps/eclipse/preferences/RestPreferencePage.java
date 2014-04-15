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
 * Revision:        $Rev: 5683 $
 * Last Changed:    $Date: 2010-10-15 11:52:51 -0500 (Fri, 15 Oct 2010) $
 * Last Changed By: $Author: jan.moxter $
 */

package org.efaps.eclipse.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.efaps.eclipse.EfapsPlugin;

/**
 * Class is used to render the Preference Page for the eFpas eclipse Plugin.
 *
 * @author The eFaps Team
 * @version $Id: PreferencePage.java 5683 2010-10-15 16:52:51Z jan.moxter $
 */
public class RestPreferencePage
    extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    public RestPreferencePage()
    {
        super(GRID);
    }

    @Override
    public void createFieldEditors() {

        addField(new ServerListEditor(PreferenceConstants.REST_URLLIST.getPrefName(), "Servers:",
                        getFieldEditorParent()));
    }

    @Override
    public void init(final IWorkbench workbench)
    {
        setPreferenceStore(EfapsPlugin.getDefault().getPreferenceStore());
        setDescription("The REST server URLS");
    }

    public class ServerListEditor
        extends ListEditor
    {

        public ServerListEditor(final String _name,
                                final String _labelText,
                                final Composite _parent)
        {
            super(_name, _labelText, _parent);
        }

        @Override
        protected String createList(final String[] items)
        {
            final StringBuilder bldr = new StringBuilder();
            boolean first= true;
            for (final String item : items) {
                if (item.length() > 0) {
                    if (first) {
                        first = false;
                    }  else {
                        bldr.append("|");
                    }
                    bldr.append(item);
                }
            }
            return bldr.toString();
        }

        @Override
        protected String getNewInputObject()
        {
            final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
            final InputDialog dialog = new InputDialog(shell, "Server", "Type Server", null, null);
            dialog.open();
            return dialog.getValue();
        }

        @Override
        protected String[] parseString(final String _stringList)
        {
            final String[] ret = _stringList.split("\\|");
            return ret;
        }
    }


}
