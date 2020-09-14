package com.sjwebb.knime.slack.nodes.user.messages.send;

import java.util.concurrent.CompletableFuture;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.SlackLocalSettingsNodeModel;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

/**
 * This is the model implementation of MessageSlackUser.
 * Send a direct message to a KNIME user
 *
 * @author Sam Webb
 */
public class MessageSlackUserNodeModel extends SlackLocalSettingsNodeModel<MessageSlackUserSettings> {


	
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

		exec.setMessage("Sending message");
		
		CompletableFuture<ChatPostMessageResponse> future = null;
		try
		{
			future = api.directMessageAsync(localSettings.getUser(), localSettings.getMessage(), localSettings.getOptionalUsername(), localSettings.getOptionalIconUrl(), localSettings.getOptionalIconEmoji());
			
			exec.setMessage("Waiting on message to complete");
			ChatPostMessageResponse response = future.get();
			
			exec.setMessage("Processing response");
			
			logResponse(response);
			
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
