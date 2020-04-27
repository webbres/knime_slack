package com.sjwebb.knime.slack.nodes.messages.send.row;

import java.util.Optional;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * The settings for the SendRowMessage node. Provides settings and dialog components
 * for handling the OAth token, selecting a column containing the channel name and
 * selecting a column containing the message to send.
 * 
 * @author Samuel
 *
 */
public class SendRowMessageSettings extends SlackOathTokenSettings {

	public static final String CONFIG_CHANNEL = "cfgChannel";
	public static final String CONFIG_MESSAGE = "cfgMessage";
	
	public static final String USERNAME =  "Username";
	public static final String SET_USERNAME = "Set-username";


	
	@Override
	protected void addSettings() {
		super.addSettings();
		
		addSetting(CONFIG_CHANNEL, new SettingsModelColumnName(CONFIG_CHANNEL, "channel"));
		addSetting(CONFIG_MESSAGE, new SettingsModelColumnName(CONFIG_MESSAGE, "message"));

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
	
	public String getUsername() {
		return getSetting(USERNAME, SettingsModelString.class).getStringValue();
	}
	
	public boolean isSetUsername()
	{
		return getBooleanValue(SET_USERNAME);
	}
	
	public Optional<String> getOptionalUsername()
	{
		return isSetUsername() ? Optional.of(getUsername()) : Optional.empty();
	}
	

	/**
	 * Get the selected column for the channel name
	 * @return
	 */
	public String getChannelColumnName()
	{
		return getColumnName(CONFIG_CHANNEL);
	}
	
	/**
	 * Get the selected column for the message
	 * @return
	 */
	public String getMessageColumnName()
	{
		return getColumnName(CONFIG_MESSAGE);
	}
	
	/**
	 * Get a column selector for the channel
	 * @return
	 */
	public DialogComponent getDialogComponentChannel()
	{
		return getDialogColumnNameSelection(CONFIG_CHANNEL, "Channel", 0, StringValue.class);
	}
	
	
	/**
	 * Get a column selector for the message
	 * @return
	 */
	public DialogComponent getDialogComponentMessage()
	{
		return getDialogColumnNameSelection(CONFIG_MESSAGE, "Message", 0, StringValue.class);
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
