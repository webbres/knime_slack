package com.sjwebb.knime.slack.nodes.users.get;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * <code>NodeDialog</code> for the "GetUsers" Node.
 * Get the users from a Sack workspace
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class GetUsersNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the GetUsers node.
     */
    protected GetUsersNodeDialog() {
    	SlackOathTokenSettings settings = new SlackOathTokenSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    }
}

