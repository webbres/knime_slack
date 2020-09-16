package com.sjwebb.knime.slack.util;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.api.SlackBotApiFactory;
import com.sjwebb.knime.slack.api.SlackBotLegacyApi;
import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.sjwebb.knime.slack.preferences.SlackPreferencePage;

public class KnimeSettingsSlackBotApiFactory {



	public static SlackBotApi getInstance() throws KnimeSlackException {
		
		if(getDefaultOAuthToken() == null || getDefaultOAuthToken().equals(""))
			throw new KnimeSlackException("No default token set in preferences");

		return SlackBotApiFactory.getInstanceForToken(getDefaultOAuthToken());
	}

	public static SlackBotApi createFromSettings(SlackOathTokenSettings localSettings) throws KnimeSlackException {
		return SlackBotApiFactory.getInstanceForToken(localSettings.getOathToken());
	}
	
	public static SlackBotLegacyApi createLegacyFromSettings(SlackOathTokenSettings localSettings) {
		return new SlackBotLegacyApi(localSettings.getOathToken());
	}
	
	public static String getDefaultOAuthToken()
	{
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.sjwebb.knime.slack.preferences");
		return prefs.getString(SlackPreferencePage.OAUTH_TOKEN);		
	}

}
