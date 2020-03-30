package com.sjwebb.knime.slack.nodes.conversation.names;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;
import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * This is the model implementation of ConversationChannelNames.
 * Use the conversations API to get the channel names
 *
 * @author Samuel Webb
 */
public class ConversationChannelNamesNodeModel extends LocalSettingsNodeModel<SlackOathTokenSettings> {
    
    /**
     * Constructor for the node model.
     */
    protected ConversationChannelNamesNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(0, 1);
    }
    /**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		SlackBotApi api = SlackBotApiFactory.createFromSettings(localSettings);

		List<String> channels = api.getChannelNamesViaConversations(false);

		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());

		for (String channel : channels) {
			container.addRowToTable(createRow(channel, container.size()));
		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
	}

	private DataRow createRow(String channel, long size) {
		DataCell[] cells = new DataCell[1];
		cells[0] = StringCellFactory.create(channel);
//		cells[1] = StringCellFactory.create(channel.getId());
//		cells[2] = StringCellFactory.create(channel.getCreator());
//		cells[3] = IntCellFactory.create(channel.getCreated());
//		cells[4] = StringCellFactory.create(channel.getPurpose().getValue());
//		cells[5] = StringCellFactory.create(channel.getTopic().getValue());

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
//		creator.addColumns(new DataColumnSpecCreator("ID", StringCellFactory.TYPE).createSpec());
//		creator.addColumns(new DataColumnSpecCreator("Creator", StringCellFactory.TYPE).createSpec());
//		creator.addColumns(new DataColumnSpecCreator("Created", IntCellFactory.TYPE).createSpec());
//		creator.addColumns(new DataColumnSpecCreator("Purpose", StringCellFactory.TYPE).createSpec());
//		creator.addColumns(new DataColumnSpecCreator("Topic", StringCellFactory.TYPE).createSpec());

		return creator.createSpec();
	}

}
