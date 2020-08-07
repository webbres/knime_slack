package com.sjwebb.knime.slack.nodes.messages.send.row;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;

import com.sjwebb.knime.slack.util.SharedSendMessageSettings;

/**
 * The settings for the SendRowMessage node. Provides settings and dialog components
 * for handling the OAth token, selecting a column containing the channel name and
 * selecting a column containing the message to send.
 * 
 * @author Samuel
 *
 */
public class SendRowMessageSettings extends SharedSendMessageSettings {

	public static final String CONFIG_CHANNEL = "cfgChannel";
	public static final String CONFIG_MESSAGE = "cfgMessage";
	

	
	@Override
	protected void addSettings() {
		super.addSettings();
		
		addSetting(CONFIG_CHANNEL, new SettingsModelColumnName(CONFIG_CHANNEL, "channel"));
		addSetting(CONFIG_MESSAGE, new SettingsModelColumnName(CONFIG_MESSAGE, "message"));

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

	

}
