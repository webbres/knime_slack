package com.sjwebb.knime.slack.util;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortType;

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
	 */
	protected void logResponse(ChatPostMessageResponse response) {
				
		SlackBotApiFactory.getInstance().logResponse(response, getLogger());

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
