package com.sjwebb.knime.slack.util;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * Provices the functionality for adding the advanced tab with the message sending configuration components
 * 
 * @author Sam Webb
 * @since 28/04/2020
 * 
 * @param <T>
 */
public class MessageSendingDialog<T extends SharedSendMessageSettings> extends DefaultNodeSettingsPane 
{
	protected T settings;
	
	public MessageSendingDialog() {
		super();
	}
	
	protected void addAdvancedSettings()
	{
    	createNewTab("Advanced");
    	createNewGroup("Username");
    	setHorizontalPlacement(true);
    	addDialogComponent(settings.getDialogComponentSetUsername());
    	addDialogComponent(settings.getDialogComponentUsername());
    	
    	createNewGroup("Icon");
    	addDialogComponent(settings.getDialogComponentSetIconUrl());
    	addDialogComponent(settings.getDialogComponentIconUrl());
    	setHorizontalPlacement(false);
    	setHorizontalPlacement(true);
       	addDialogComponent(settings.getDialogComponentSetIconEmoji());
    	addDialogComponent(settings.getDialogComponentIconEmoji());
	}

}
