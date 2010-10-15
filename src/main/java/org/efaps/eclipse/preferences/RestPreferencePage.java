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

    public void init(final IWorkbench workbench)
    {
        setPreferenceStore(EfapsPlugin.getDefault().getPreferenceStore());
        setDescription("A demonstration of a preference page implementation");
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
