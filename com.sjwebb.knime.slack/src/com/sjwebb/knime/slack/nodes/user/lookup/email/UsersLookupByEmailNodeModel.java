package com.sjwebb.knime.slack.nodes.user.lookup.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.MissingCell;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.AbstractCellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.api.SlackBotApiFactory;
import com.sjwebb.knime.slack.util.SlackLocalSettingsNodeModel;
import com.slack.api.methods.response.channels.UsersLookupByEmailResponse;
import com.slack.api.model.User;


/**
 * <code>NodeModel</code> for the "UsersLookupByEmail" node.
 *
 * @author Samuel Webb
 */
public class UsersLookupByEmailNodeModel extends SlackLocalSettingsNodeModel<UserLookupByEmailSettings> 
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception 
    {
        BufferedDataTable in = inData[0];
        
        SlackBotApi api = SlackBotApiFactory.getInstanceForToken(localSettings.getOathToken());
    	
        BufferedDataTable table = exec.createColumnRearrangeTable(in, createColumnRearranger(in.getDataTableSpec(), api), exec);
        
    	
        return new BufferedDataTable[]{table};
    }

    /**
     * Create a column rearranger that appends the user details to the input row
     * @param original
     * @return
     */
    private ColumnRearranger createColumnRearranger(DataTableSpec original, SlackBotApi api) 
    {
		ColumnRearranger rearranger = new ColumnRearranger(original);	
		rearranger.append(new UsersLookupByEmailCellFactory(createOutputSpec(original), localSettings, api, original.findColumnIndex(localSettings.getEmailColumnName()), getLogger()));
		
		return rearranger;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException 
    {
    	DataTableSpecCreator creator = new DataTableSpecCreator(inSpecs[0]);
    	creator.addColumns(createOutputSpec(inSpecs[0]));
    	
        return new DataTableSpec[]{creator.createSpec()};
    }
    
    /**
     * Create the spec for the additional columns
     * @return
     */
	private DataColumnSpec[] createOutputSpec(DataTableSpec spec) 
	{
		List<DataColumnSpec> creator = new ArrayList<DataColumnSpec>();
		
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "ID"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Name"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Color"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Two factor type"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Email"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Real name"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Phone"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Skype"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Presence"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Display name"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Display name normalised"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Status text"), StringCellFactory.TYPE).createSpec());
		creator.add(new DataColumnSpecCreator(DataTableSpec.getUniqueColumnName(spec, "Image"), StringCellFactory.TYPE).createSpec());
		
		return creator.toArray(new DataColumnSpec[] {});
	}

}



class UsersLookupByEmailCellFactory extends AbstractCellFactory
{
	private SlackBotApi api;
	private int index;
	private int numOut;
	
	private NodeLogger logger;
	
	public UsersLookupByEmailCellFactory(DataColumnSpec[] additional, UserLookupByEmailSettings localSettings, SlackBotApi api, int index, NodeLogger logger)
	{
		super(false, additional);
		this.api = api;
		this.index = index;
		this.numOut = additional.length;
		this.logger = logger;
	}
	
	@Override
	public DataCell[] getCells(DataRow row) 
	{
		DataCell in = row.getCell(index);
		
		DataCell[] out = null;
		
		if(in.isMissing())
		{
			out = createMissingRow("Input email missing");
		} else
		{
			String email = ((StringValue) in).getStringValue();
			
			UsersLookupByEmailResponse response;
			
			try 
			{
				response = api.getUser(email);
			
			
				if(!response.isOk())
				{
					String error = response.getError() + " - " + (response.getNeeded() != null ? " needed: " + response.getNeeded() : "");
									
					logger.error("Failed to fetch user", new Exception(error));
					out = createMissingRow(error);
				} else
				{
					out = createCells(response.getUser());
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
				out = createMissingRow(e.getMessage());
			}
		}
		
		return out;
	}
	
	private DataCell[] createCells(User user)
	{
		DataCell[] cells = new DataCell[numOut];
		
		cells[0] = user.getId() != null ? StringCellFactory.create(user.getId()) : new MissingCell("No value available");
		cells[1] = user.getName() != null ? StringCellFactory.create(user.getName()) : new MissingCell("No value available");
		cells[2] = user.getColor() != null ? StringCellFactory.create(user.getColor()) : new MissingCell("No value available");
		cells[3] = user.getTwoFactorType() != null ? StringCellFactory.create(user.getTwoFactorType()) : new MissingCell("No value available");
		cells[4] = user.getProfile().getEmail() != null ? StringCellFactory.create(user.getProfile().getEmail()) : new MissingCell("No value available");
		cells[5] = user.getProfile().getRealName() != null ? StringCellFactory.create(user.getProfile().getRealName()) : new MissingCell("No value available");		
		cells[6] = user.getProfile().getPhone() != null ? StringCellFactory.create(user.getProfile().getPhone()) : new MissingCell("No value available");		
		cells[7] = user.getProfile().getSkype() != null ? StringCellFactory.create(user.getProfile().getSkype()) : new MissingCell("No value available");		
		cells[8] = user.getPresence() != null ? StringCellFactory.create(user.getPresence()) : new MissingCell("No value available");	
		cells[9] = user.getProfile().getDisplayName() != null ? StringCellFactory.create(user.getProfile().getDisplayName()) : new MissingCell("No value available");
		cells[10] = user.getProfile().getDisplayNameNormalized() != null ? StringCellFactory.create(user.getProfile().getDisplayNameNormalized()) : new MissingCell("No value available");	
		cells[11] = user.getProfile().getStatusText() != null ? StringCellFactory.create(user.getProfile().getStatusText()) : new MissingCell("No value available");
		cells[12] = user.getProfile().getImageOriginal() != null ? StringCellFactory.create(user.getProfile().getImageOriginal()) : new MissingCell("No value available");
		
		return cells;
	}

	private DataCell[] createMissingRow(String message) 
	{
		DataCell[] cells = new DataCell[numOut];
		Arrays.fill(cells, new MissingCell(message));
		return cells;
	}
	
	
}