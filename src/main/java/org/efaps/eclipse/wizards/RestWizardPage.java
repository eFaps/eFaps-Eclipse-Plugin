
package org.efaps.eclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.preferences.PreferenceConstants;

public class RestWizardPage
    extends WizardPage
{
    private Text text1;
    private Composite container;
    private Combo comboUrl;

    public Combo getComboUrl()
    {
        return this.comboUrl;
    }

    /**
     * @param _pageName
     */
    protected RestWizardPage(final String _pageName)
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
        label1.setText("URL");

        this.comboUrl = new Combo(this.container, SWT.READ_ONLY);
        this.comboUrl.setSize(200, 220);

        final String[] urls = EfapsPlugin.getDefault().getPreferenceStore()
                .getString(PreferenceConstants.REST_URLLIST.getPrefName()).split("\\|");

        for (final String url : urls) {
            this.comboUrl.add(url);
        }
        this.comboUrl.select(0);

        final Label label2 = new Label(this.container, SWT.NULL);
        label2.setText("Password");

        this.text1 = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.text1.setText("");
        this.text1.addKeyListener(new KeyListener() {

            public void keyPressed(final KeyEvent e) {
            }

            public void keyReleased(final KeyEvent e) {
                if (RestWizardPage.this.text1.getText().length()>0) {
                    setPageComplete(true);
                }
            }

        });
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        this.text1.setLayoutData(gd);
        // Required to avoid an error in the system
        setControl(this.container);
        setPageComplete(true);
    }
}
