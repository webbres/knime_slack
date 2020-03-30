package com.sjwebb.knime.slack.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class SlackPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String OAUTH_TOKEN = "OAuth Token";
	
	public SlackPreferencePage() {
		super(GRID);
	}
	
	

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.sjwebb.knime.slack.preferences"));
        setDescription("Slack Integration preferences");

	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(OAUTH_TOKEN, "OAuth Access Token", getFieldEditorParent()));

	}

}
