package com.sjwebb.knime.slack.nodes.user.lookup.email;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "UsersLookupByEmail" node.
 * 
 * @author Samuel Webb
 */
public class UsersLookupByEmailNodeDialog extends DefaultNodeSettingsPane 
{

    /**
     * New pane for configuring the UsersLookupByEmail node.
     */
    protected UsersLookupByEmailNodeDialog() 
    {
    	UserLookupByEmailSettings settings = new UserLookupByEmailSettings();
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	addDialogComponent(settings.getDialogComponentEmailColumnNameSelection());
    }
}

