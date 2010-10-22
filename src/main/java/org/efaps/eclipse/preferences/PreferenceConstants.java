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
package org.efaps.eclipse.preferences;

/**
 * Constant definitions for plug-in preferences
 *
 * @author The eFaps Team
 * @version $Id: PreferencePage.java 5683 2010-10-15 16:52:51Z jan.moxter $
 */
public enum PreferenceConstants
{
    REST_PWD("org.efaps.eclipse.rest.password"),
    REST_USER("org.efaps.eclipse.rest.user"),
    REST_URLLIST("org.efaps.eclipse.rest.list");

    private String prefName;

    public String getPrefName()
    {
        return this.prefName;
    }

    private PreferenceConstants(final String _prefName)
    {
        this.prefName = _prefName;
    }

}
