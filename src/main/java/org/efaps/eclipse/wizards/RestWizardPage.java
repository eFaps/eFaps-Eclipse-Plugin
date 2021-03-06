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
package org.efaps.eclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.efaps.eclipse.EfapsPlugin;
import org.efaps.eclipse.preferences.PreferenceConstants;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class RestWizardPage
    extends WizardPage
{

    private Text passwd;
    private Composite container;
    private Combo comboUrl;
    private Text user;

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

    @Override
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

        final String pwd = EfapsPlugin.getDefault().getPreferenceStore()
                        .getString(PreferenceConstants.REST_PWD.getPrefName());
        final String user = EfapsPlugin.getDefault().getPreferenceStore()
                        .getString(PreferenceConstants.REST_USER.getPrefName());

        final Label label3 = new Label(this.container, SWT.NULL);
        label3.setText("User");

        this.user = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.user.setText(user);
        this.user.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(final ModifyEvent e)
            {
                EfapsPlugin.getDefault()
                                .getPreferenceStore()
                                .setValue(PreferenceConstants.REST_USER.getPrefName(),
                                                RestWizardPage.this.user.getText());
            }
        });

        final Label label2 = new Label(this.container, SWT.NULL);
        label2.setText("Password");

        this.passwd = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.passwd.setText(pwd);
        this.passwd.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(final ModifyEvent e)
            {
                EfapsPlugin.getDefault()
                                .getPreferenceStore()
                                .setValue(PreferenceConstants.REST_PWD.getPrefName(),
                                                RestWizardPage.this.passwd.getText());
            }

        });
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        this.passwd.setLayoutData(gd);
        this.user.setLayoutData(gd);
        // Required to avoid an error in the system
        setControl(this.container);
        setPageComplete(true);
    }
}
