package com.sjwebb.knime.slack.nodes.messages.send.row;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "SendRowMessage" Node.
 * Send a message based on contents selected from a table row.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class SendRowMessageNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the SendRowMessage node.
     */
    protected SendRowMessageNodeDialog() {
    	
    	SendRowMessageSettings settings = new SendRowMessageSettings();
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	
    	createNewGroup("Message details");
    	setHorizontalPlacement(true);
    	addDialogComponent(settings.getDialogComponentChannel());
    	addDialogComponent(settings.getDialogComponentMessage());

    }
}

