package com.sjwebb.knime.slack.nodes.channels.get;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * <code>NodeDialog</code> for the "GetChannels" Node.
 * Get the channels from the connected Slack workspace. * n * nThe oath token(s) must be specified in the KNIME preferences. 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Samuel Webb
 */
public class GetChannelsNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the GetChannels node.
     */
    protected GetChannelsNodeDialog() {

    	SlackOathTokenSettings settings = new SlackOathTokenSettings();
    	
    	addDialogComponent(settings.getDialogCompoinentOathToken());
    	
    }
}

