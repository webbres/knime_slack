package com.sjwebb.knime.slack.nodes.messages.send;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.sjwebb.knime.slack.util.SlackLocalSettingsNodeModel;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

/**
 * This is the model implementation of SendMessage. Send a slack message to a
 * designated channel
 *
 * @author Samuel Webb
 */
public class SendMessageNodeModel extends SlackLocalSettingsNodeModel<SlackSendMessageSettings> {


	
	/**
	 * Constructor for the node model.
	 */
	protected SendMessageNodeModel() {		
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
		
//		Optional<Channel> channel = api.findChannelWithName(localSettings.getChannel());
		
		if(localSettings.lookupConversation() && !api.channelExists(localSettings.getChannel()))
		{
			throw new KnimeSlackException("Provided channel name of " + localSettings.getChannel() + " is not valid");
		}
//		
//		api.postMessage(channel.get(), localSettings.getMessage());
		exec.setMessage("Sending message");
		
		CompletableFuture<ChatPostMessageResponse> future = api.sendMessageToChannelAsync(
				localSettings.getChannel(), 
				localSettings.getMessage(), 
				localSettings.getOptionalUsername(), 
				localSettings.getOptionalIconUrl(), 
				localSettings.getOptionalIconEmoji(), 
				localSettings.lookupConversation()
				);
		
		
		exec.setMessage("Waiting on message send to complete");
		
		ChatPostMessageResponse response = future.get();
		
		exec.setMessage("Message sent");
		
		logResponse(response);
		
		if(!response.isOk())
		{
			if(response.getError().equals("missing_scope")) {
				throw new IOException("Failed to post message: " + response.getError() + " " + response.getNeeded());
			} else {
				throw new IOException("Failed to post message: " + response.getError());
			}

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
	protected void reset() {
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
