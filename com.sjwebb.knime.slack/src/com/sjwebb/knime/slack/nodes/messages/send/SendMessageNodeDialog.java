package com.sjwebb.knime.slack.nodes.messages.send;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "SendMessage" Node.
 * Send a slack message to a designated channel
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class SendMessageNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the SendMessage node.
     */
    protected SendMessageNodeDialog() {
    	SlackSendMessageSettings settings = new SlackSendMessageSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	addDialogComponent(settings.getDialogCompoinentChannel());
    	addDialogComponent(settings.getDialogCompoinentMessage());
    	
    }
}

