package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import com.sjwebb.knime.slack.util.MessageSendingDialog;

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
public class MessageSlackUserNodeDialog extends MessageSendingDialog<MessageSlackUserSettings> {

    /**
     * New pane for configuring the MessageSlackUser node.
     */
    protected MessageSlackUserNodeDialog() 
    {
    	super();
    	super.settings = new MessageSlackUserSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	addDialogComponent(settings.getDialogComponentUser());
    	addDialogComponent(settings.getDialogCompoinentMessage());
    	addDialogComponent(settings.getDialogComponentFailOnError());
    

    
    	addAdvancedSettings();
    }
}

