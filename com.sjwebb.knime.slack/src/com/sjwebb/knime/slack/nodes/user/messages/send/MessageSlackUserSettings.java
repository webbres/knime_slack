package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.SharedSendMessageSettings;

public class MessageSlackUserSettings extends SharedSendMessageSettings {

	public static final String USER = "channel";
	public static final String MESSAGE = "Message";
	public static final String FAIL_ON_ERROR = "failOnError";
	

	
	@Override
	protected void addSettings() {
		super.addSettings();
		addSetting(USER, new SettingsModelString(USER, ""));
		addSetting(MESSAGE, new SettingsModelString(MESSAGE, "Hello from KNIME"));
		addSetting(FAIL_ON_ERROR, new SettingsModelBoolean(FAIL_ON_ERROR, true));
	}

	
	public boolean isFailOnError()
	{
		return getBooleanValue(FAIL_ON_ERROR);
	}
	
	
	public String getMessage()
	{
		return getSetting(MESSAGE, SettingsModelString.class).getStringValue();
	}
	
	public String getUser()
	{
		return getSetting(USER, SettingsModelString.class).getStringValue();
	}
	
	public DialogComponent getDialogCompoinentMessage()
	{
		return new DialogComponentMultiLineString(getSetting(MESSAGE, SettingsModelString.class), "Message to send");
	}
	
	public DialogComponent getDialogComponentUser()
	{
		return new DialogComponentString(getSetting(USER, SettingsModelString.class), "User to send message to");
	}
	
	public DialogComponent getDialogComponentFailOnError()
	{
		return getBooleanComponent(FAIL_ON_ERROR, "Fail on error");
	}
	

}
