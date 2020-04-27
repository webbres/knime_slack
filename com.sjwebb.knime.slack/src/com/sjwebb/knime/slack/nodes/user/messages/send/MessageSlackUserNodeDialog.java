package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "MessageSlackUser" Node.
 * Send a direct message to a KNIME user
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Sam Webb
 */
public class MessageSlackUserNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the MessageSlackUser node.
     */
    protected MessageSlackUserNodeDialog() 
    {
    	MessageSlackUserSettings settings = new MessageSlackUserSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	addDialogComponent(settings.getDialogComponentUser());
    	addDialogComponent(settings.getDialogCompoinentMessage());
    	addDialogComponent(settings.getDialogComponentFailOnError());
    
    	createNewGroup("Username");
    	setHorizontalPlacement(true);
    	addDialogComponent(settings.getDialogComponentSetUsername());
    	addDialogComponent(settings.getDialogComponentUsername());
    }
}

