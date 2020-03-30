package com.sjwebb.knime.slack.nodes.conversation.names;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * <code>NodeDialog</code> for the "ConversationChannelNames" Node.
 * Use the conversations API to get the channel names
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class ConversationChannelNamesNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the GetChannels node.
     */
    protected ConversationChannelNamesNodeDialog() {

    	SlackOathTokenSettings settings = new SlackOathTokenSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	
    }
}

