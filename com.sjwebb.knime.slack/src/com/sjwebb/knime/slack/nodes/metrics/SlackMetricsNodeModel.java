package com.sjwebb.knime.slack.nodes.metrics;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.data.json.JSONCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjwebb.knime.slack.api.SlackBotApi;
import com.sjwebb.knime.slack.api.SlackBotApiFactory;
import com.slack.api.methods.MethodsStats;
import com.slack.api.methods.metrics.MetricsDatastore;

/**
 * <code>NodeModel</code> for the "SlackMetrics" node.
 *
 * @author Samuel Webb
 */
public class SlackMetricsNodeModel extends NodeModel 
{
    
    /**
     * Constructor for the node model.
     */
    protected SlackMetricsNodeModel() 
    {
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception 
    {
		ObjectMapper objectMapper = new ObjectMapper();
		
		BufferedDataContainer container = exec.createDataContainer(createSpec());
    	
    	
    	Map<String, SlackBotApi> bots = SlackBotApiFactory.getINSTANCES();
    	
		if (bots.isEmpty()) 
		{
			getLogger().error("No SlackBotApi instances have been configured.");
		}
    	
    	for (SlackBotApi bot : bots.values()) 
    	{
    		MetricsDatastore metrics = bot.getMetrics();
    		
    		Map<String, Map<String, MethodsStats>> obj = metrics.getAllStats();
    		
    		for (Entry<String, Map<String, MethodsStats>> element : obj.entrySet()) 
    		{
    			for(Entry<String, MethodsStats> team : element.getValue().entrySet())
    			{
    				String name = team.getKey();
    				MethodsStats stats = team.getValue();
    				
    				String json = objectMapper.writeValueAsString(stats);
    				
    				container.addRowToTable(createRow(name, json, container.size()));
    			}
    		}
    	}
    	
    	container.close();

        return new BufferedDataTable[]{container.getTable()};
    }

    
    
    /**
     * Create a new output row
     * @param name		the name of the team
     * @param stats		the JSON string for the stats
     * @param index		the current row index
     * @return
     * @throws IOException
     */
    private DataRow createRow(String name, String stats, long index) throws IOException 
    {
    	DataCell nameCell = StringCellFactory.create(name);
		DataCell cell = JSONCellFactory.create(stats, false);
		
		return new DefaultRow(new RowKey("Row" + index), nameCell, cell);
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException 
    {    	
        return new DataTableSpec[]{createSpec() };
    }
    
    /**
     * Create the output table spec
     * @return
     */
    private DataTableSpec createSpec() 
    {
    	DataTableSpecCreator creator = new DataTableSpecCreator();
    	creator.addColumns(new DataColumnSpecCreator("Team", StringCellFactory.TYPE).createSpec());
    	creator.addColumns(new DataColumnSpecCreator("Stats", JSONCellFactory.TYPE).createSpec());
    	
    	return creator.createSpec();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) 
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException 
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException 
    {

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException 
    {

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException 
    {

    }

}

