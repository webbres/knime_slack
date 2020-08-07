package com.sjwebb.knime.slack.nodes.messages.send;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.SharedSendMessageSettings;

public class SlackSendMessageSettings extends SharedSendMessageSettings {

	public static final String CHANNEL = "channel";
	public static final String MESSAGE = "Message";

	
	@Override
	protected void addSettings() {
		super.addSettings();
		

		addSetting(CHANNEL, new SettingsModelString(CHANNEL, ""));
		addSetting(MESSAGE, new SettingsModelString(MESSAGE, "Hello from KNIME"));
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
