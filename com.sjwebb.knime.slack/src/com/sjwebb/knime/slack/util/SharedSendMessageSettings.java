package com.sjwebb.knime.slack.util;

import java.util.Optional;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Settings for nodes that can send messages
 * 
 * @author Sam Webb
 * @since 28/04/2020
 */
public class SharedSendMessageSettings extends SlackOathTokenSettings 
{
	
	public static final String USERNAME =  "Username";
	public static final String SET_USERNAME = "Set-username";
	
	public static final String ICON_URL =  "ICON_URL";
	public static final String SET_ICON_URL = "SET_ICON_URL";
	
	public static final String ICON_EMOJI =  "ICON_EMOJI";
	public static final String SET_ICON_EMOJI = "SET_ICON_EMOJI";
	
	
	private static final String DEFAULT_URL = "https://forum-cdn.knime.com/uploads/default/original/1X/ab3ccf34482a0329361734a18199390177204f15.png";
	private static final String DEFAULT_EMOJI = ":chart_with_upwards_trend:";
	
	public static final String CONFIG_LOOKUP = "cfgLookup";

	@Override
	protected void addSettings() 
	{
		super.addSettings();
		
		// Username
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
		
		// Icon URL
		SettingsModelString iconUrl = new SettingsModelString(ICON_URL, DEFAULT_URL);
		iconUrl.setEnabled(false);
		
		addSetting(ICON_URL, iconUrl);

		SettingsModelBoolean setIconUrl = new SettingsModelBoolean(SET_ICON_URL, false);
		addSetting(SET_ICON_URL, setIconUrl);
		
		
		setIconUrl.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				iconUrl.setEnabled(setIconUrl.getBooleanValue());
			}
		});
		
		// Icon Emoji
		SettingsModelString iconEmoji = new SettingsModelString(ICON_EMOJI, DEFAULT_EMOJI);
		iconEmoji.setEnabled(false);
		
		addSetting(ICON_EMOJI, iconEmoji);

		SettingsModelBoolean setOconEmoji = new SettingsModelBoolean(SET_ICON_EMOJI, false);
		addSetting(SET_ICON_EMOJI, setOconEmoji);
		
		
		setOconEmoji.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				iconEmoji.setEnabled(setOconEmoji.getBooleanValue());
			}
		});
				
		addSetting(CONFIG_LOOKUP, new SettingsModelBoolean(CONFIG_LOOKUP, false));
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
	
	public Optional<String> getOptionalIconUrl()
	{
		return getBooleanValue(SET_ICON_URL) ? Optional.of(getSetting(ICON_URL, SettingsModelString.class).getStringValue()) : Optional.empty();
	}
	
	public Optional<String> getOptionalIconEmoji()
	{
		return getBooleanValue(SET_ICON_EMOJI) ? Optional.of(getSetting(ICON_EMOJI, SettingsModelString.class).getStringValue()) : Optional.empty();
	}
	
	
	public DialogComponent getDialogComponentUsername()
	{
		return new DialogComponentString(getSetting(USERNAME, SettingsModelString.class), "Username");
	}
	
	public DialogComponent getDialogComponentSetUsername()
	{
		return new DialogComponentBoolean(getSetting(SET_USERNAME, SettingsModelBoolean.class), "Set username");
	}	
	
	public DialogComponent getDialogComponentIconUrl()
	{
		return new DialogComponentString(getSetting(ICON_URL, SettingsModelString.class), "Icon URL");
	}
	
	public DialogComponent getDialogComponentSetIconUrl()
	{
		return new DialogComponentBoolean(getSetting(SET_ICON_URL, SettingsModelBoolean.class), "Set Icon URL");
	}	
	
	public DialogComponent getDialogComponentIconEmoji()
	{
		return new DialogComponentString(getSetting(ICON_EMOJI, SettingsModelString.class), "Icon emoji");
	}
	
	public DialogComponent getDialogComponentSetIconEmoji()
	{
		return new DialogComponentBoolean(getSetting(SET_ICON_EMOJI, SettingsModelBoolean.class), "Set Icon emoji");
	}
	
	
	/**
	 * Dialog component for whether to lookup the conversation of use the channel name directly 
	 * 
	 * @return
	 */
	public DialogComponent getDialogComponentLookup()
	{
		return getBooleanComponent(CONFIG_LOOKUP, "Lookup conversation");
	}


	/**
	 * Whether to lookup the conversation of use the channel name directly 
	 * @return
	 */
	public boolean lookupConversation() {
		return getBooleanValue(CONFIG_LOOKUP);
	}
	
}
