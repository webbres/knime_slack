package com.sjwebb.knime.slack.util;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.preferences.SlackPreferencePage;

public class SlackBotApiFactory {

	private static SlackBotApi API;

	public static SlackBotApi getInstance() {

		if (API == null)
			API = new SlackBotApi(getDefaultOAuthToken());

		return API;
	}

	public static SlackBotApi createFromSettings(SlackOathTokenSettings localSettings) {
		return new SlackBotApi(localSettings.getOathToken());
	}
	
	public static String getDefaultOAuthToken()
	{
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.sjwebb.knime.slack.preferences");
		return prefs.getString(SlackPreferencePage.OAUTH_TOKEN);		
	}

}
