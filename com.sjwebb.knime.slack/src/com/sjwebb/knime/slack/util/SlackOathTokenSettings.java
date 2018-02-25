package com.sjwebb.knime.slack.util;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Handles the settings and creation of dialog component for the OAth token.
 * 
 * @author Sam
 *
 */
public class SlackOathTokenSettings extends NodeSettingCollection {

	public static final String OATH_TOKEN = "oathToken";
	
	/**
	 * Must overide this method calling super.addSettings() then add additional settings
	 * 
	 * <pre>
	 * 	protected void addSettings() 
	 *  	{
	 *		super.addSettings();
	 *		
	 *		// Your settings here
	 *  	}
	 * </pre>
	 */
	@Override
	protected void addSettings() {
		addSetting(OATH_TOKEN, new SettingsModelString(OATH_TOKEN, getPreferenceOAuthToken()));

	}

	/**
	 * Get the OAtuh token from the preferences. This is used if not replaced in the individual dialog
	 * instance.
	 * 
	 * @return
	 */
	private String getPreferenceOAuthToken() 
	{
		return SlackBotApiFactory.getDefaultOAuthToken();
	}
	
	/**
	 * Get the OAth token specified in the dialog
	 * 
	 * @return
	 */
	public String getOathToken()
	{
		return getSetting(OATH_TOKEN, SettingsModelString.class).getStringValue();
	}
	
	/**
	 * Get a dialog component allowing the user to change the OAth token from that specified
	 * in the preferences. It is initially populated with the token from the preferences.
	 * 
	 * @return
	 */
	public DialogComponent getDialogCompoinentOathToken()
	{
		return new DialogComponentString(getSetting(OATH_TOKEN, SettingsModelString.class), "Bot OAuth token");
	}

}
