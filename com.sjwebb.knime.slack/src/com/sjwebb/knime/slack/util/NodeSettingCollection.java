package com.sjwebb.knime.slack.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.knime.core.data.DataValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

/**
 * This class is GPLv3 and taken from: the Lhasa Limited KNIME community contribution
 * https://bitbucket.org/lhasaLimited/knime-community-contribution
 * <br><br>
 * 
 * Abstract node settings. Provides the basic implementation of save, load and
 * validate. <br>
 * </br>
 * Can optionally extend this abstract class to do the leg work with managing
 * the settings. <br>
 * </br>
 * Implement the {@link #addSettings()} method which is called by the default no
 * args constructor. <br>
 * </br>
 * Supporting methods can be added to get the values from the settings. Or the
 * the {@link SettingsModel} can be retrieved, but this requires explicit
 * knowledge of the class.
 * 
 * @author Samuel, Lhasa Limited
 * @since 1.2.000
 *
 */
public abstract class NodeSettingCollection {

	protected Map<String, SettingsModel> settingMap;

	/**
	 * The add settings method should add all the nodes settings to the
	 * {@link #settingMap}. <br>
	 * </br>
	 * <b>Example:</b>
	 * 
	 * <pre>
	 *		<code>
	 *		&#64;Override
	 *		protected void addSettings()
	 *		{
	 *			addSetting(CONFIG_STRUCTURE_COLUMN, new SettingsModelColumnName(CONFIG_STRUCTURE_COLUMN, ""));
	 *			addSetting(CONFIG_IMAGE_FORMAT, new SettingsModelString(CONFIG_IMAGE_FORMAT, "PNG"));
	 *			addSetting(CONFIG_IMAGE_WIDTH, new SettingsModelInteger(CONFIG_IMAGE_WIDTH, 250));
	 *			addSetting(CONFIG_IMAGE_HEIGHT, new SettingsModelInteger(CONFIG_IMAGE_HEIGHT, 250));
	 *			addSetting(CONFIG_WITH_FILL_TO_FIT, new SettingsModelBoolean(CONFIG_WITH_FILL_TO_FIT, true));
	 *		}
	 * 		</code>
	 * </pre>
	 *
	 */
	protected abstract void addSettings();

	/**
	 * Add a new setting to the map. This will overide previous settings with
	 * the same name.
	 * 
	 * @param key
	 *            The settings unique key
	 * @param model
	 *            The settings model boject
	 */
	protected void addSetting(String key, SettingsModel model) {
		settingMap.put(key, model);
	}

	public NodeSettingCollection() {
		super();
		settingMap = new HashMap<String, SettingsModel>();
		addSettings();
	}

	/**
	 * Iteratively calls {@link SettingsModel#loadSettingsFrom(NodeSettingsRO)}
	 * on all the settings storedO
	 * 
	 * @param settings
	 * @throws InvalidSettingsException
	 */
	public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		for (SettingsModel setting : settingMap.values()) {
			try {
				setting.loadSettingsFrom(settings);
			} catch (Exception e) {
				throw new InvalidSettingsException(e);
			}
		}
	}

	/**
	 * @param settings
	 * @deprecated see {@link #saveSettingsTo(NodeSettingsWO)}
	 */
	public void saveSettings(final NodeSettingsWO settings) {
		saveSettingsTo(settings);
		// for (SettingsModel setting : settingMap.values())
		// {
		// try
		// {
		// setting.saveSettingsTo(settings);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// throw new UncheckedIOException("", new IOException(e));
		// }
		// }

	}

	/**
	 * Calls the {@link #loadSettings(NodeSettingsRO)} method
	 * 
	 * @param settings
	 * @deprecated Call {@link #loadSettings(NodeSettingsRO)} directly
	 */
	public void loadSettingsForDialog(final NodeSettingsRO settings) {
		try {
			loadSettings(settings);
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Iterator for the stored {@link SettingsModel} objects
	 * 
	 * @return
	 */
	public Iterator<SettingsModel> getSettingIterator() {
		return settingMap.values().iterator();
	}

	/**
	 * Get the desired setting cast to the given type.
	 * 
	 * @param key
	 *            The key (see the static strings)
	 * @param type
	 *            The class of the {@link SettingsModel}
	 * @return
	 */
	public <T> T getSetting(String key, Class<T> type) {
		return (T) settingMap.get(key);
	}

	/**
	 * Iteratively call {@link SettingsModel#validateSettings} all all stored
	 * settings
	 */
	public void validateSettings(NodeSettingsRO settings) {
		for (SettingsModel s : settingMap.values()) {

			try {
				s.validateSettings(settings);
			} catch (InvalidSettingsException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Iteratively call {@link SettingsModel#loadSettingsFrom} all all stored
	 * settings
	 */
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) {
		for (SettingsModel s : settingMap.values()) {
			try {
				s.loadSettingsFrom(settings);
			} catch (InvalidSettingsException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Iteratively call {@link SettingsModel#saveSettingsTo} all all stored
	 * settings
	 */
	public void saveSettingsTo(NodeSettingsWO settings) {

		for (SettingsModel model : settingMap.values()) {
			model.saveSettingsTo(settings);
		}
	}

	/**
	 * Get the string value from a {@link SettingsModelColumnName}
	 * 
	 * @param key
	 * @return
	 */
	protected String getColumnName(String key) {
		return getSetting(key, SettingsModelColumnName.class).getColumnName();
	}

	/**
	 * Get the integer value from a {@link SettingsModelInteger}
	 * 
	 * @param key
	 * @return
	 */
	protected int getInteger(String key) {
		return getSetting(key, SettingsModelInteger.class).getIntValue();
	}

	/**
	 * Get the boolean value from a {@link SettingsModelBoolean}
	 * 
	 * @param string
	 * @return
	 */
	protected boolean getBooleanValue(String string) {
		return getSetting(string, SettingsModelBoolean.class).getBooleanValue();
	}

	/**
	 * Get the double value from the {@link SettingsModelDouble}
	 * 
	 * @param key
	 * @return
	 */
	protected double getDouble(String key) {
		return getSetting(key, SettingsModelDouble.class).getDoubleValue();
	}

	/**
	 * Get a new {@link DialogComponentColumnNameSelection} dialog component
	 * 
	 * @param key
	 *            The unique key for the setting
	 * @param name
	 *            The text to display in th edialog
	 * @param index
	 *            The input table index
	 * @param classFilter
	 *            The class filter
	 * 
	 * @return
	 */
	protected DialogComponent getDialogColumnNameSelection(String key, String name, int index,
			final Class<? extends DataValue>... classFilter) {
		return new DialogComponentColumnNameSelection(getSetting(key, SettingsModelColumnName.class), name, index,
				classFilter);
	}

	/**
	 * Create a {@link DialogComponentBoolean} for the given setting and using
	 * the given label.
	 * 
	 * @param key
	 *            The unique key for the setting
	 * @param label
	 *            The label to display in the dialog
	 * @return
	 */
	protected DialogComponent getBooleanComponent(String key, String label) {
		return new DialogComponentBoolean(getSetting(key, SettingsModelBoolean.class), label);
	}

}