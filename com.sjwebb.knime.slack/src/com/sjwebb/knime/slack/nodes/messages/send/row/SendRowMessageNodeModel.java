package com.sjwebb.knime.slack.nodes.messages.send.row;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.MissingCell;
import org.knime.core.data.StringValue;
import org.knime.core.data.append.AppendedColumnRow;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;

/**
 * This is the model implementation of SendRowMessage. Send a message based on
 * contents selected from a table row.
 *
 * @author Samuel Webb
 */
public class SendRowMessageNodeModel extends LocalSettingsNodeModel<SendRowMessageSettings> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		BufferedDataTable in = inData[0];

		int channelIndex = in.getSpec().findColumnIndex(localSettings.getChannelColumnName());
		int messageIndex = in.getSpec().findColumnIndex(localSettings.getMessageColumnName());

		BufferedDataContainer container = exec.createDataContainer(createOutputSpec(in.getSpec()));
		
		SlackBotApi api;

		try 
		{
			api = SlackBotApiFactory.createFromSettings(localSettings);
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception("Could not establish SlackBotApi", e);
		}

		for (DataRow row : in) 
		{
			DataCell channelCell = row.getCell(channelIndex);
			DataCell messageCell = row.getCell(messageIndex);

			if (channelCell.isMissing() || messageCell.isMissing()) 
			{
				addErrorRow(container, row, "Channel or message cell is missing");
			} else 
			{
				String channel = ((StringValue) channelCell).getStringValue();
				String message = ((StringValue) messageCell).getStringValue();


				if (api.channelExists(channel)) 
				{
					try 
					{
						String timestamp = api.sendMessageToChannel(
								channel, 
								message, 
								localSettings.getOptionalUsername(), 
								localSettings.getOptionalIconUrl(), 
								localSettings.getOptionalIconEmoji(), 
								localSettings.lookupConversation());
						addRow(container, row, timestamp);
					} catch (Exception e) 
					{
						e.printStackTrace();
						addErrorRow(container, row, e.getMessage());
					}

				} else {
					addErrorRow(container, row, "Channel does not exist");
				}
			}

		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
	}

	private void addRow(BufferedDataContainer container, DataRow row, String timestamp) {

		DataCell outCell;
		try 
		{
			outCell = StringCellFactory.create(timestamp);
		} catch (Exception e) 
		{
			e.printStackTrace();
			getLogger().error(e);
			outCell = new MissingCell("Failed to generated cell: " + e.getMessage());
		}

		DataRow outRow = new AppendedColumnRow(row, outCell);
		container.addRowToTable(outRow);

	}

	private void addErrorRow(BufferedDataContainer container, DataRow row, String message) {

		DataRow outRow = new AppendedColumnRow(row, new MissingCell(message));
		container.addRowToTable(outRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return new DataTableSpec[] { createOutputSpec(inSpecs[0]) };
	}

	private DataTableSpec createOutputSpec(DataTableSpec inSpec) {
		DataTableSpecCreator creator = new DataTableSpecCreator(inSpec);

		creator.addColumns(
				new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(inSpec, "timestamp"), StringCellFactory.TYPE)
						.createSpec());

		return creator.createSpec();
	}

}
