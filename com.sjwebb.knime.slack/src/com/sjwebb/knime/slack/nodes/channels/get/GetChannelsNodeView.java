package com.sjwebb.knime.slack.nodes.channels.get;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "GetChannels" Node.
 * Get the channels from the connected Slack workspace. * n * nThe oath token(s) must be specified in the KNIME preferences. 
 *
 * @author Samuel Webb
 */
public class GetChannelsNodeView extends NodeView<GetChannelsNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link GetChannelsNodeModel})
     */
    protected GetChannelsNodeView(final GetChannelsNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

