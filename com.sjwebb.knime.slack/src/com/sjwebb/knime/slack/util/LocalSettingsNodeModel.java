package com.sjwebb.knime.slack.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortType;

/**
 * This class is GPLv3 and taken from: the Lhasa Limited KNIME community contribution
 * https://bitbucket.org/lhasaLimited/knime-community-contribution
 * <br><br>
 * An extension to the {@link NodeModel} class where all settings are managed through an
 * instance of {@link INodeSettingCollection}. The settings object can be accessed via
 * the localSetting variable.
 * <br><br>
 * See {@link StreamableLocalSettingsNodeModel} for the streaming implementation
 * 
 * @author Samuel, Lhasa Limited
 * @since 1.2.000
 * @param <T> 	An instance of {@link NodeSettingCollection} which should be used for handling of all the 
 * 				node settings.
 */
public abstract class LocalSettingsNodeModel<T extends NodeSettingCollection> extends NodeModel
{

	protected T localSettings;
	
	public LocalSettingsNodeModel() {
		this(1, 1);
	}
	
	protected LocalSettingsNodeModel(int nrInDataPorts, int nrOutDataPorts)
	{
		super(nrInDataPorts, nrOutDataPorts);
		setSettingsObject();
		
	}
	
	/**
	 * Creates a new instance of the {@link INodeSettingCollection} object
	 */
	protected void setSettingsObject()
	{

		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

		try
		{
			localSettings = (T) Class
					.forName(parameterizedType.getActualTypeArguments()[0].toString().replaceAll("class ", ""), true, getClass().getClassLoader())
					.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	protected LocalSettingsNodeModel(PortType[] inPortTypes, PortType[] outPortTypes)
	{
		super(inPortTypes, outPortTypes);
	}


	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
	{
		localSettings.saveSettingsTo(settings);
		
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException
	{
		localSettings.validateSettings(settings);
		
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException
	{
		localSettings.loadValidatedSettingsFrom(settings);
	}
	
	/**
	 * Get the settings for the node
	 * @return
	 */
	public T getSettingsCollection()
	{
		return localSettings;
	}
	

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException
	{
		
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException
	{
		
		
	}
	
	@Override
	protected void reset()
	{
		
	}
}