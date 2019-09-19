package com.sjwebb.knime.slack.nodes.channels.get;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell.BooleanCellFactory;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.github.seratch.jslack.api.model.Conversation;
import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;
import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * This is the model implementation of GetChannels. Get the channels from the
 * connected Slack workspace. n nThe oath token(s) must be specified in the
 * KNIME preferences.
 *
 * @author Samuel Webb
 */
public class GetChannelsNodeModel extends LocalSettingsNodeModel<SlackOathTokenSettings> {

	/**
	 * Constructor for the node model.
	 */
	protected GetChannelsNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		SlackBotApi api = SlackBotApiFactory.createFromSettings(localSettings);

//		List<Channel> channels = api.getChannelList();
		List<Conversation> conversations = api.getConversations(false, true);

		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());

		
		for (Conversation conv : conversations) {
			container.addRowToTable(createRow(conv, container.size()));
		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
	}

	private DataRow createRow(Conversation channel, long size) {
		DataCell[] cells = new DataCell[8];
		cells[0] = StringCellFactory.create(channel.getName());
		cells[1] = StringCellFactory.create(channel.getId());
		cells[2] = StringCellFactory.create(channel.getCreator());
		cells[3] = IntCellFactory.create(channel.getCreated());
		cells[4] = StringCellFactory.create(channel.getPurpose().getValue());
		cells[5] = StringCellFactory.create(channel.getTopic().getValue());
		cells[6] = BooleanCellFactory.create(channel.isPrivate());
		cells[7] = BooleanCellFactory.create(channel.isArchived());

		DefaultRow row = new DefaultRow(new RowKey("Row" + size), cells);

		return row;
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

	/**
	 * Create the output table specification
	 * 
	 * @return
	 */
	private DataTableSpec createOutputSpec() {
		DataTableSpecCreator creator = new DataTableSpecCreator();
		creator.addColumns(new DataColumnSpecCreator("Name", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("ID", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Creator", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Created", IntCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Purpose", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Topic", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Private", BooleanCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Archived", BooleanCellFactory.TYPE).createSpec());

		return creator.createSpec();
	}

}
