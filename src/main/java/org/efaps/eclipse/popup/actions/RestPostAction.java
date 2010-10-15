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

public class RestPostAction
    implements IObjectActionDelegate
{

    private Shell shell;
    private TreeSelection selection;

    /**
     * Constructor for Action1.
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
