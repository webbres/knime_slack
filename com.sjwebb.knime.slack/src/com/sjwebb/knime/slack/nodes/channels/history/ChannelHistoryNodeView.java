package com.sjwebb.knime.slack.nodes.channels.history;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ChannelHistory" Node.
 * Get the channel history for the defined channel
 *
 * @author Samuel Webb
 */
public class ChannelHistoryNodeView extends NodeView<ChannelHistoryNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ChannelHistoryNodeModel})
     */
    protected ChannelHistoryNodeView(final ChannelHistoryNodeModel nodeModel) {
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

