package com.sjwebb.knime.slack.nodes.user.messages.send;

import java.util.Optional;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.NodeSettingCollection;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;

public class MessageSlackUserSettings extends NodeSettingCollection {

	public static final String OATH_TOKEN = "oathToken";
	public static final String USER = "channel";
	public static final String MESSAGE = "Message";
	public static final String FAIL_ON_ERROR = "failOnError";
	
	public static final String USERNAME =  "Username";
	public static final String SET_USERNAME = "Set-username";
	
	@Override
	protected void addSettings() {
		addSetting(OATH_TOKEN, new SettingsModelString(OATH_TOKEN, getPreferenceOAuthToken()));
		addSetting(USER, new SettingsModelString(USER, ""));
		addSetting(MESSAGE, new SettingsModelString(MESSAGE, "Hello from KNIME"));
		addSetting(FAIL_ON_ERROR, new SettingsModelBoolean(FAIL_ON_ERROR, true));
		
		SettingsModelString username = new SettingsModelString(USERNAME, "KNIME Bot");
		username.setEnabled(false);
		
		addSetting(USERNAME, username);

		SettingsModelBoolean setUsername = new SettingsModelBoolean(SET_USERNAME, false);
		addSetting(SET_USERNAME, setUsername);
		
		
		setUsername.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				username.setEnabled(setUsername.getBooleanValue());
			}
		});
	}

	
	public boolean isFailOnError()
	{
		return getBooleanValue(FAIL_ON_ERROR);
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
		return new DialogComponentString(getSetting(OATH_TOKEN, SettingsModelString.class), "OAuth token");
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
	
	public Optional<String> getOptionalUsername()
	{
		return isSetUsername() ? Optional.of(getUsername()) : Optional.empty();
	}
	
	
	public String getUsername() {
		return getSetting(USERNAME, SettingsModelString.class).getStringValue();
	}
	
	public boolean isSetUsername()
	{
		return getBooleanValue(SET_USERNAME);
	}
	
	public DialogComponent getDialogComponentUsername()
	{
		return new DialogComponentString(getSetting(USERNAME, SettingsModelString.class), "Username");
	}
	
	public DialogComponent getDialogComponentSetUsername()
	{
		return new DialogComponentBoolean(getSetting(SET_USERNAME, SettingsModelBoolean.class), "Set username");
	}	
}
