package com.sjwebb.knime.slack.nodes.messages.send;

import java.util.Optional;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.github.seratch.jslack.api.model.Channel;
import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;

/**
 * This is the model implementation of SendMessage. Send a slack message to a
 * designated channel
 *
 * @author Samuel Webb
 */
public class SendMessageNodeModel extends LocalSettingsNodeModel<SlackSendMessageSettings> {

	/**
	 * Constructor for the node model.
	 */
	protected SendMessageNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		SlackBotApi api = new SlackBotApi(localSettings.getOathToken());
		
		Optional<Channel> channel = api.findChannelWithName(localSettings.getChannel());
		
		if(!channel.isPresent())
		{
			throw new Exception("Provided channel name of " + localSettings.getChannel() + " is not valid");
		}
		
		api.postMessage(channel.get(), localSettings.getMessage());
		
		return inData;
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

		return inSpecs;
	}

}
