package com.sjwebb.knime.slack.nodes.channels.history2;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

public class ChannelHistorySettings2 extends SlackOathTokenSettings {

	public static final String CHANNEL_NAME = "channelName";
	
	@Override
	protected void addSettings() {
		super.addSettings();
		
		addSetting(CHANNEL_NAME, new SettingsModelString(CHANNEL_NAME, ""));
	}
	
	public String getChannelName()
	{
		return getSetting(CHANNEL_NAME, SettingsModelString.class).getStringValue();
	}
	
	public DialogComponent getDialogComponentChannelNameSelection()
	{
		return new DialogComponentString(getSetting(CHANNEL_NAME, SettingsModelString.class), "Channel");
	}

}
