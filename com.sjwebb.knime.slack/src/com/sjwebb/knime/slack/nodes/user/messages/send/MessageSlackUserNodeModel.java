package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

/**
 * This is the model implementation of MessageSlackUser.
 * Send a direct message to a KNIME user
 *
 * @author Sam Webb
 */
public class MessageSlackUserNodeModel extends LocalSettingsNodeModel<MessageSlackUserSettings> {


	
	/**
	 * Constructor for the node model.
	 */
	protected MessageSlackUserNodeModel() {		
		super(new PortType[] {PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class, true)}, 
				new PortType[] {PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class, false)});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		SlackBotApi api = new SlackBotApi(localSettings.getOathToken());

		ChatPostMessageResponse response = null;
		try
		{
			response = api.directMessage(localSettings.getUser(), localSettings.getMessage(), localSettings.getOptionalUsername());
			
			if(!response.isOk())
			{
				String error = response.getError() + " - " + response.getMessage() + (response.getNeeded() != null ? " needed: " + response.getNeeded() : "");
				
				getLogger().error(error);
				
				
				setWarningMessage(response.getError() + " - " + response.getMessage());
				
				throw new Exception(error);
			}
		} catch (Exception e)
		{			
			getLogger().error(e);
			
			if(localSettings.isFailOnError())
				throw e;
		}

		
		BufferedDataTable[] out;
		
		if(inData.length == 0 || inData[0] == null)
		{
			out = new BufferedDataTable[] {createEmptyTable(exec)};
		} else
		{
			out = inData;
		}
		
		return out;
	}


	private BufferedDataTable createEmptyTable(ExecutionContext exec) 
	{
		BufferedDataContainer container = exec.createDataContainer(getEmptySpec());
		container.close();
		
		
		return container.getTable();
	}

	private DataTableSpec getEmptySpec() 
	{
		DataTableSpecCreator creator = new DataTableSpecCreator();
		return creator.createSpec();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() 
	{
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return (inSpecs.length == 0 || inSpecs[0] == null) ? new DataTableSpec[] {getEmptySpec()} : inSpecs;
	}

}
