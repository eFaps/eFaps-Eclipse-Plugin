
package org.efaps.eclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CompileWizardPage
    extends WizardPage
{
    private Composite container;
    private Button checkJasper;

    private Button checkJava;
    private Button checkCss;

    /**
     * @param _pageName
     */
    protected CompileWizardPage(final String _pageName)
    {
        super(_pageName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(final Composite _parent)
    {
        this.container = new Composite(_parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        final Label label1 = new Label(this.container, SWT.NULL);
        label1.setText("Jasper");
        this.checkJasper = new Button(this.container, SWT.CHECK);
        this.checkJasper.setSelection(true);

        final Label label2 = new Label(this.container, SWT.NULL);
        label2.setText("Java");
        this.checkJava = new Button(this.container, SWT.CHECK);
        this.checkJava.setSelection(true);


        final Label label3 = new Label(this.container, SWT.NULL);
        label3.setText("Css");
        this.checkCss = new Button(this.container, SWT.CHECK);
        this.checkCss.setSelection(true);


        // Required to avoid an error in the system
        setControl(this.container);
        setPageComplete(true);
    }



    public Button getCheckCss()
    {
        return this.checkCss;
    }

    public Button getCheckJasper()
    {
        return this.checkJasper;
    }


    public Button getCheckJava()
    {
        return this.checkJava;
    }
}
