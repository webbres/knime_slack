package com.sjwebb.knime.slack.nodes.user.lookup.email;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;

import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

public class UserLookupByEmailSettings extends SlackOathTokenSettings
{

	public static final String CFG_EMAIL = "cfgEmail";
	
	@Override
	protected void addSettings() 
	{
		super.addSettings();
		
		addSetting(CFG_EMAIL, new SettingsModelColumnName(CFG_EMAIL, ""));
	}
	
	public String columnName()
	{
		return getColumnName(CFG_EMAIL);
	}
	
	public DialogComponent getDialogComponentEmailColumnNameSelection()
	{
		return getDialogColumnNameSelection(CFG_EMAIL, "Email", 0, StringValue.class);
	}

	public String getEmailColumnName() 
	{
		return getColumnName(CFG_EMAIL);
	}
	
	
}
