package com.sjwebb.knime.slack.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class SlackPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String OATH_TOKEN = "Oath Token";
	
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
		addField(new StringFieldEditor(OATH_TOKEN, "Bot User OAuth Access Token", getFieldEditorParent()));

	}

}
