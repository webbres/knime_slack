package com.sjwebb.knime.slack.nodes.channels.history;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "ChannelHistory" Node.
 * Get the channel history for the defined channel
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class ChannelHistoryNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ChannelHistory node.
     */
    protected ChannelHistoryNodeDialog() {
    	ChannelHistorySettings settings = new ChannelHistorySettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	addDialogComponent(settings.getDialogComponentChannelNameSelection());
    }
}

