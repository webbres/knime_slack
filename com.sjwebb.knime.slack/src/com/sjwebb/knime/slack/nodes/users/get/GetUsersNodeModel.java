package com.sjwebb.knime.slack.nodes.users.get;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
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

import com.github.seratch.jslack.api.model.User;
import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.util.LocalSettingsNodeModel;
import com.sjwebb.knime.slack.util.SlackBotApiFactory;
import com.sjwebb.knime.slack.util.SlackOathTokenSettings;

/**
 * This is the model implementation of GetUsers.
 * Get the users from a Sack workspace
 *
 * @author Samuel Webb
 */
public class GetUsersNodeModel extends LocalSettingsNodeModel<SlackOathTokenSettings> {
	
	private static final int NUM_OUT_CELLS = 8;
    
    /**
     * Constructor for the node model.
     */
    protected GetUsersNodeModel() {
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

		SlackBotApi api;
		try {
			api = SlackBotApiFactory.createFromSettings(localSettings);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Could not create API object. Do you have a valid token?", e);
		}
		
		List<User> users;
		try {
			users = api.getUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		BufferedDataTable table = createOutputTable(exec, users);
		
        return new BufferedDataTable[]{table};
    }

    private BufferedDataTable createOutputTable(ExecutionContext exec, List<User> users) {
    	
		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());
		
		for(User user : users)
		{
			DataCell[] cells = new DataCell[NUM_OUT_CELLS];
			
			cells[0] = user.getId() != null ? StringCellFactory.create(user.getId()) : new MissingCell("No value available");
			cells[1] = user.getName() != null ? StringCellFactory.create(user.getName()) : new MissingCell("No value available");
			cells[2] = user.getColor() != null ? StringCellFactory.create(user.getColor()) : new MissingCell("No value available");
			cells[3] = user.getTwoFactorType() != null ? StringCellFactory.create(user.getTwoFactorType()) : new MissingCell("No value available");
			cells[4] = user.getProfile().getEmail() != null ? StringCellFactory.create(user.getProfile().getEmail()) : new MissingCell("No value available");
			cells[5] = user.getProfile().getRealName() != null ? StringCellFactory.create(user.getProfile().getRealName()) : new MissingCell("No value available");		
			cells[6] = user.getProfile().getPhone() != null ? StringCellFactory.create(user.getProfile().getPhone()) : new MissingCell("No value available");		
			cells[7] = user.getProfile().getSkype() != null ? StringCellFactory.create(user.getProfile().getSkype()) : new MissingCell("No value available");		
			
			
			container.addRowToTable(new DefaultRow(new RowKey("Row" + container.size()), cells));
		}
		
		container.close();
		
		return container.getTable();
	}

	private DataTableSpec createOutputSpec() {

		DataTableSpecCreator creator = new DataTableSpecCreator();
		
		creator.addColumns(new DataColumnSpecCreator("ID", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Name", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Color", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Two factor type", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Email", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Real name", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Phone", StringCellFactory.TYPE).createSpec());
		creator.addColumns(new DataColumnSpecCreator("Skype", StringCellFactory.TYPE).createSpec());
		
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        return new DataTableSpec[]{createOutputSpec()};
    }

    
}

