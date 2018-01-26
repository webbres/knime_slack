package com.sjwebb.knime.slack.nodes.messages.send;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.NodeSettingCollection;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;

public class SlackSendMessageSettings extends NodeSettingCollection {

	public static final String OATH_TOKEN = "oathToken";
	public static final String CHANNEL = "channel";
	public static final String MESSAGE = "Message";
	
	@Override
	protected void addSettings() {
		addSetting(OATH_TOKEN, new SettingsModelString(OATH_TOKEN, getPreferenceOAuthToken()));
		addSetting(CHANNEL, new SettingsModelString(CHANNEL, ""));
		addSetting(MESSAGE, new SettingsModelString(MESSAGE, "Hello from KNIME"));

	}

	private String getPreferenceOAuthToken() 
	{
		return SlackBotApiFactory.getDefaultOAuthToken();
	}
	
	public String getOathToken()
	{
		return getSetting(OATH_TOKEN, SettingsModelString.class).getStringValue();
	}
	
	public DialogComponent getDialogCompoinentOathToken()
	{
		return new DialogComponentString(getSetting(OATH_TOKEN, SettingsModelString.class), "Bot OAuth token");
	}
	
	public String getMessage()
	{
		return getSetting(MESSAGE, SettingsModelString.class).getStringValue();
	}
	
	public String getChannel()
	{
		return getSetting(CHANNEL, SettingsModelString.class).getStringValue();
	}
	
	public DialogComponent getDialogCompoinentMessage()
	{
		return new DialogComponentMultiLineString(getSetting(MESSAGE, SettingsModelString.class), "Message to send");
	}
	
	public DialogComponent getDialogCompoinentChannel()
	{
		return new DialogComponentString(getSetting(CHANNEL, SettingsModelString.class), "Channel to send message to");
	}
	

}
