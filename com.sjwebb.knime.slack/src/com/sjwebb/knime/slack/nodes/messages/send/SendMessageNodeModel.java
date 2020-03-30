package com.sjwebb.knime.slack.nodes.messages.send;

import java.util.Optional;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import com.github.seratch.jslack.api.model.Channel;
import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.exception.KnimeSlackException;
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
		
		if(!api.channelExists(localSettings.getChannel()))
		{
			throw new KnimeSlackException("Provided channel name of " + localSettings.getChannel() + " is not valid");
		}
//		
//		api.postMessage(channel.get(), localSettings.getMessage());
		
		api.sendMessageToChannel(localSettings.getChannel(), localSettings.getMessage());
		
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

	private DataTableSpec getEmptySpec() {
		
		DataTableSpecCreator creator = new DataTableSpecCreator();
		return creator.createSpec();
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
