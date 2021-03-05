package com.sjwebb.knime.slack.util;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortType;

import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackLocalSettingsNodeModel<T extends NodeSettingCollection> extends LocalSettingsNodeModel<T> 
{

	public SlackLocalSettingsNodeModel() {
		super();
	}
	

	public SlackLocalSettingsNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) {
		super(inPortTypes, outPortTypes);
	}

	/**
	 * Prints some debug info to the {@link NodeLogger} instance
	 * @param response
	 * @throws KnimeSlackException 
	 */
	protected void logResponse(ChatPostMessageResponse response) throws KnimeSlackException {
				
		try 
		{
			NodeLogger logger = getLogger();
			logger.debug("Slack node class: " + getClass());
			logger.debug("Response: " + response);
			
			if (response != null) {
				logger.debug("Chat message isOk: " + response.isOk());
				logger.debug("Chat message errors: " + response.getError());
				logger.debug("Chat message warning: " + response.getWarning());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	protected BufferedDataTable createEmptyTable(ExecutionContext exec) 
	{
		BufferedDataContainer container = exec.createDataContainer(getEmptySpec());
		container.close();
		
		
		return container.getTable();
	}

	protected DataTableSpec getEmptySpec() 
	{
		DataTableSpecCreator creator = new DataTableSpecCreator();
		return creator.createSpec();
	}
	
}
