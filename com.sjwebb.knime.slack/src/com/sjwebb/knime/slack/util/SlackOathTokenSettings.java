package com.sjwebb.knime.slack.util;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SlackOathTokenSettings extends NodeSettingCollection {

	public static final String OATH_TOKEN = "oathToken";
	
	@Override
	protected void addSettings() {
		addSetting(OATH_TOKEN, new SettingsModelString(OATH_TOKEN, getPreferenceOAuthToken()));

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

}
