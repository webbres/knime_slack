package com.sjwebb.knime.slack.nodes.channels.history;

import java.util.List;
import java.util.Optional;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.api.SlackBotLegacyApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;
import com.slack.api.model.Channel;
import com.slack.api.model.Message;

/**
 * This is the model implementation of ChannelHistory. Get the channel history
 * for the defined channel
 *
 * @author Samuel Webb
 */
public class ChannelHistoryNodeModel extends LocalSettingsNodeModel<ChannelHistorySettings> {

	/**
	 * Constructor for the node model.
	 */
	protected ChannelHistoryNodeModel() 
	{

		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		SlackBotLegacyApi api = SlackBotApiFactory.createLegacyFromSettings(localSettings);
		
		getLogger().debug(api.checkAuth());

		Optional<Channel> channel = api.findChannelWithName(localSettings.getChannelName());

		if (!channel.isPresent()) {
			throw new Exception("Provided channel name of " + localSettings.getChannelName() + " is not valid");
		}

		List<Message> messages;
		
		try 
		{
			messages = api.getChannelMessages(channel.get(), Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		BufferedDataTable table = createOutputTable(exec, messages);

		return new BufferedDataTable[] { table };
	}

	private BufferedDataTable createOutputTable(ExecutionContext exec, List<Message> messages) {

		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());

		for (Message msg : messages)
			container.addRowToTable(createRow(msg, container.size()));

		container.close();

		return container.getTable();
	}

	private DataRow createRow(Message msg, long index) {

		DataCell[] cells = new DataCell[] {
				createCell(msg.getUser()),
				createCell(msg.getUsername()),
				createCell(msg.getBotId()),
				createCell(msg.getSubtype()),
				createCell(msg.getTs()),
				createCell(msg.getThreadTs()),
				createCell(msg.getText())
		};

		return new DefaultRow(new RowKey("Row" + index), cells);
	}

	private DataCell createCell(String string) {
		
		return string == null ? new MissingCell("No data") : StringCellFactory.create(string);
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

		return new DataTableSpec[] { createOutputSpec() };
	}

	private DataTableSpec createOutputSpec() {
		DataTableSpecCreator creator = new DataTableSpecCreator();
		
		creator.addColumns(new DataColumnSpecCreator("User", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Username", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Bot ID", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Subtype", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Timestamp", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Thread timestamp", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Text", StringCellFactory.TYPE).createSpec());
		
		return creator.createSpec();
	}

}
