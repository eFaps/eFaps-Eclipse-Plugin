package org.efaps.eclipse.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public enum PreferenceConstants
{

    REST_SERVERLIST("org.efaps.eclipse.rest.list");

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
